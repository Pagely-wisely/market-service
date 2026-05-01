package com.pagely.marketservice.domain.event;

import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;

public interface OrderEvents {
    void orderCreated(OrderCreatedEvent event);

    void orderCancelled(OrderCancelledEvent event);
}
