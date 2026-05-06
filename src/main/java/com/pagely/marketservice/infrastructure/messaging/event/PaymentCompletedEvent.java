package com.pagely.marketservice.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(
        String eventId,
        String eventType,
        String domainType,
        String domainId,
        Instant occurredAt,
        Payload payload
) {
    public record Payload(
            UUID orderId,
            UUID paymentId,
            UUID buyerId,
            UUID sellerId,
            int amount,
            String approvedAt
    ) {
    }
}
