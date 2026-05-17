package com.pagely.marketservice.infrastructure.messaging.outbox;

import com.pagely.marketservice.infrastructure.messaging.kafka.producer.KafkaEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {
    private static final int BATCH_SIZE = 100;

    private final KafkaEventPublisher kafkaEventPublisher;
    private final OutboxManageService outboxManageService;

    // Outbox에 적재된 미발행 이벤트들을 주기적으로 Kafka 메시지로 발행
    @Scheduled(fixedDelayString = "${outbox.poll-interval-ms}")
    public void publishUnpublishedEvents() {
        List<OutboxEvent> events = outboxManageService.claimPublishTargets(BATCH_SIZE);

        if (events.isEmpty()) {
            return;
        }

        log.debug("[OutboxPoller] 미발행 이벤트 : {}건", events.size());

        for (OutboxEvent event : events) {
            kafkaEventPublisher.publish(event);
        }
    }
}
