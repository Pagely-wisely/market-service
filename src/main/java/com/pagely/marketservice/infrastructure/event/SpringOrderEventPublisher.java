package com.pagely.marketservice.infrastructure.event;

import com.pagely.marketservice.domain.event.OrderEvents;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringOrderEventPublisher implements OrderEvents {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void orderCreated(OrderCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void orderCancelled(OrderCancelledEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
