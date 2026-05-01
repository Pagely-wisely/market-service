package com.pagely.marketservice.application.port.out;

import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;

public interface OrderEventPort {
    void publishOrderCreated(OrderCreatedEvent event);

    void publishOrderCancelled(OrderCancelledEvent event);
}
