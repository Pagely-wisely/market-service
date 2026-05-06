package com.pagely.marketservice.application.dto.command;

import java.util.UUID;

public record AcceptOrderCommand(
        UUID orderId
) {
}
