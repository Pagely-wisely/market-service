package com.pagely.marketservice.infrastructure.messaging.outbox;

public interface OutboxRepository {
    OutboxEvent save(OutboxEvent event);
}
