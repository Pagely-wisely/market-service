package com.pagely.marketservice.infrastructure.messaging.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, updatable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "topic", nullable = false, length = 255)
    private String topic;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "publishing", nullable = false)
    private boolean publishing;

    @Column(name = "publishing_started_at")
    private LocalDateTime publishingStartedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "failure_count", nullable = false)
    private int failureCount;

    @Column(name = "last_failed_at")
    private LocalDateTime lastFailedAt;

    @Column(name = "last_failure_message", columnDefinition = "TEXT")
    private String lastFailureMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static OutboxEvent of(
            UUID id,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String topic,
            String payload
    ) {
        OutboxEvent outbox = new OutboxEvent();
        outbox.id = id;
        outbox.aggregateType = aggregateType;
        outbox.aggregateId = aggregateId;
        outbox.eventType = eventType;
        outbox.topic = topic;
        outbox.payload = payload;
        outbox.published = false;
        outbox.publishing = false;
        outbox.failureCount = 0;
        outbox.createdAt = LocalDateTime.now();
        return outbox;
    }

    // 발행 시작
    public void startPublishing() {
        this.publishing = true;
        this.publishingStartedAt = LocalDateTime.now();
    }

    // 발행 성공
    public void markPublished() {
        this.publishing = false;
        this.published = true;
        this.publishedAt = LocalDateTime.now();
    }

    // 발행 실패
    public void markFailed(String errorMessage) {
        this.publishing = false;
        this.publishingStartedAt = null;
        this.failureCount++;
        this.lastFailedAt = LocalDateTime.now();
        this.lastFailureMessage = errorMessage;
    }
}
