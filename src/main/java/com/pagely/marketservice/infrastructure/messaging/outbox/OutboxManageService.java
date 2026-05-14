package com.pagely.marketservice.infrastructure.messaging.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.marketservice.domain.event.BaseEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutboxManageService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OutboxEvent saveOutbox(BaseEvent event, String topic) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.of(
                    UUID.fromString(event.getEventId()),
                    event.getDomainType(),
                    UUID.fromString(event.getDomainId()),
                    event.getEventType(),
                    topic,
                    payload
            );

            return outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("Outbox 이벤트 직렬화 실패. eventType={}", event.getEventType(), e);
            throw new IllegalStateException("Outbox 이벤트 직렬화 실패: " + event.getEventType(), e);
        }
    }
}
