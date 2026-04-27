package com.pagely.marketservice.application.dto.command;

import java.util.UUID;

public record CreateSalePostCommand(
        UUID sellerId,
        String bookId,
        String title,
        String description,
        int price,
        String condition
) {
}
