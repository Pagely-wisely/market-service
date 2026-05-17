package com.pagely.marketservice.infrastructure.messaging.outbox;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = """
               SELECT * FROM p_outbox
            WHERE published = false AND publishing = false                                                                                                                         \s
            ORDER BY created_at ASC
            LIMIT :limit
            FOR UPDATE SKIP LOCKED                                                                                                                                                 \s
            """,
            nativeQuery = true
    )
    public List<OutboxEvent> findUnpublishedEvents(@Param("limit") int limit);
}
