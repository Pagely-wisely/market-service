package com.pagely.marketservice.infrastructure.messaging.outbox;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOutboxRepository extends JpaRepository<OutboxEvent, UUID> {
}
