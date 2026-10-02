package com.pagely.marketservice.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.pagely.marketservice.application.dto.command.CreateOrderCommand;
import com.pagely.marketservice.application.service.OrderService;
import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.repository.SalePostRepository;
import com.pagely.marketservice.infrastructure.messaging.outbox.OutboxPoller;
import com.pagely.marketservice.infrastructure.persistence.JpaOrderRepository;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.kafka.bootstrap-servers=localhost:29999",
                "spring.kafka.consumer.auto-startup=false"
        }
)
@Testcontainers
class ConcurrentOrderTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("market_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @MockitoBean
    OutboxPoller outboxPoller; // OutboxPoller가 실행되면 KafkaTemplate을 호출하므로 차단

    @Autowired
    OrderService orderService;

    @Autowired
    SalePostRepository salePostRepository;

    @Autowired
    JpaOrderRepository jpaOrderRepository;

    @Test
    void 동일_판매글에_동시_주문_시_1건만_성공해야_한다() throws InterruptedException {
        // given
        SalePost salePost = salePostRepository.save(
                SalePost.create(UUID.randomUUID(), "BOOK-001", "동시성 테스트용 책", "테스트 설명", 10_000, "GOOD")
        );
        UUID salePostId = salePost.getId();

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            UUID buyerId = UUID.randomUUID();
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    orderService.createOrder(new CreateOrderCommand(buyerId, salePostId));
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // then
        long orderCount = jpaOrderRepository.findAll().stream()
                .filter(o -> o.getSalePost().getId().equals(salePostId))
                .count();

        System.out.println("=== 동시성 테스트 결과 ===");
        System.out.println("성공 요청 수: " + successCount.get());
        System.out.println("실패 요청 수: " + failCount.get());
        System.out.println("DB 주문 생성 수: " + orderCount);

        assertThat(successCount.get())
                .as("주문 성공은 정확히 1건이어야 하지만 %d건 성공했습니다 → 중복 주문 발생", successCount.get())
                .isEqualTo(1);

        assertThat(orderCount)
                .as("DB에 생성된 주문은 1건이어야 하지만 %d건 존재합니다 → Overselling 발생", orderCount)
                .isEqualTo(1);
    }
}
