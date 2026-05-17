package com.pagely.marketservice.infrastructure.event;

import com.pagely.marketservice.application.event.OrderEventHandler;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;
import com.pagely.marketservice.infrastructure.messaging.kafka.OrderTopics;
import com.pagely.marketservice.infrastructure.messaging.outbox.OutboxManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventHandlerAdapter implements OrderEventHandler {
    private final OutboxManageService outboxManageService;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        outboxManageService.saveOutbox(event, OrderTopics.ORDER_CREATED);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void HandleOrderCancelled(OrderCancelledEvent event) {
        outboxManageService.saveOutbox(event, OrderTopics.ORDER_CANCELLED);
    }
}
