package com.pagely.marketservice.application.dto.command;

import java.util.UUID;

public record RegisterTrackingNumberCommand(
        UUID sellerId,
        UUID orderId,
        String trackingNumber,
        String courierCompany
) {
}
