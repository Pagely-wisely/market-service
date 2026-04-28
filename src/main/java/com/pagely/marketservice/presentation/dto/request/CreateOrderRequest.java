package com.pagely.marketservice.presentation.dto.request;

import com.pagely.marketservice.application.dto.command.CreateOrderCommand;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "판매글 ID는 필수입니다.")
        UUID salePostId

) {
    public CreateOrderCommand toCommand(UUID buyerId) {
        return new CreateOrderCommand(
                buyerId,
                salePostId
        );
    }
}
