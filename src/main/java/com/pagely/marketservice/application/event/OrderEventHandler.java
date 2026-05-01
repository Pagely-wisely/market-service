package com.pagely.marketservice.application.event;

import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;

public interface OrderEventHandler {
    void handleOrderCreated(OrderCreatedEvent event);

    void HandleOrderCancelled(OrderCancelledEvent event);
}
