package com.pagely.marketservice.infrastructure.messaging.kafka.producer;

import com.pagely.marketservice.application.port.out.OrderEventPort;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;

public class KafkaOrderEventAdapter implements OrderEventPort {
    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        
    }

    @Override
    public void publishOrderCancelled(OrderCancelledEvent event) {

    }
}
