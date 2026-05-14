package com.pagely.marketservice.infrastructure.messaging.outbox;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaOutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("""
            select o
            from OutboxEvent o
            where o.published = false
            and o.publishing = false
            order by o.createdAt asc 
            """)
    public List<OutboxEvent> findUnpublishedEvents(Pageable pageable);
}
