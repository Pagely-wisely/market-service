package com.pagely.marketservice.infrastructure.messaging.outbox;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboxRepository {
    OutboxEvent save(OutboxEvent event);

    Optional<OutboxEvent> findById(UUID id);

    List<OutboxEvent> findUnpublishedEvents(int limit);
}
