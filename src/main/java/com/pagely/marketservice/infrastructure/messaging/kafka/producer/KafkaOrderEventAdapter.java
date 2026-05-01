package com.pagely.marketservice.infrastructure.messaging.kafka.producer;

import com.pagely.marketservice.application.port.out.OrderEventPort;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventAdapter implements OrderEventPort {

    private static final String ORDER_CREATED_TOPIC = "order-created";
    private static final String ORDER_CANCELLED_TOPIC = "order-cancelled";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        publish(ORDER_CREATED_TOPIC, event.getDomainId(), event);
    }

    @Override
    public void publishOrderCancelled(OrderCancelledEvent event) {
        publish(ORDER_CANCELLED_TOPIC, event.getDomainId(), event);
    }

    private void publish(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka 발생 실패 topic: {} key: {}", topic, key);
                    } else {
                        log.info("Kafka 발행 성공 topic: {} key: {} offset: {}",
                                topic, key, result.getRecordMetadata().offset());
                    }
                });
    }
}
