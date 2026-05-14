package com.pagely.marketservice.infrastructure.messaging.outbox;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final JpaOutboxRepository jpaOutboxRepository;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        return jpaOutboxRepository.save(event);
    }

    @Override
    public Optional<OutboxEvent> findById(UUID id) {
        return jpaOutboxRepository.findById(id);
    }

    @Override
    public List<OutboxEvent> findUnpublishedEvents(Pageable pageable) {
        return jpaOutboxRepository.findUnpublishedEvents(pageable);
    }
}
