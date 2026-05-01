package com.pagely.marketservice.infrastructure.event;

import com.pagely.marketservice.application.event.OrderEventHandler;
import com.pagely.marketservice.application.port.out.OrderEventPort;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventHandlerAdapter implements OrderEventHandler {

    private final OrderEventPort orderEventPort;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        orderEventPort.publishOrderCreated(event);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void HandleOrderCancelled(OrderCancelledEvent event) {
        orderEventPort.publishOrderCancelled(event);
    }
}
