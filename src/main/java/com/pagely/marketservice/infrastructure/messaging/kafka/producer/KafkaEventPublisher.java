package com.pagely.marketservice.infrastructure.messaging.kafka.producer;

import com.pagely.marketservice.infrastructure.messaging.outbox.OutboxEvent;
import com.pagely.marketservice.infrastructure.messaging.outbox.OutboxManageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboxManageService outboxManageService;

    public void publish(OutboxEvent event) {
        String topic = event.getTopic();
        String key = event.getAggregateId().toString();
        String payload = event.getPayload();
        UUID outboxId = event.getId();

        kafkaTemplate
                .send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    // 실패 처리
                    if (ex != null) {
                        outboxManageService.recordPublishFailure(outboxId, ex.getMessage());

                        log.error("Kafka 발생 실패 topic: {}, outboxId: {}, key: {}", topic, outboxId, key);
                        return;
                    }

                    // 성공 처리
                    outboxManageService.markPublished(outboxId);

                    log.debug("Kafka 발행 성공 topic: {}, outboxId: {}, key: {} offset: {}",
                            topic, outboxId, key, result.getRecordMetadata().offset());
                });
    }

}
