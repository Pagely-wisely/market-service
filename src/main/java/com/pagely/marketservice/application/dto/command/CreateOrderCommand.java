package com.pagely.marketservice.application.dto.command;

import java.util.UUID;

public record CreateOrderCommand(
        UUID buyerId,
        UUID salePostId
) {
}
