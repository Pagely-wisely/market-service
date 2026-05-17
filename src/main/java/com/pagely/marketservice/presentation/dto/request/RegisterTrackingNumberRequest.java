package com.pagely.marketservice.presentation.dto.request;

import com.pagely.marketservice.application.dto.command.RegisterTrackingNumberCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegisterTrackingNumberRequest(
        @NotBlank(message = "운송장 번호는 필수입니다.")
        @Size(max = 50, message = "운송장 번호는 50자 이하여야 합니다.")
        String trackingNumber,

        @NotBlank(message = "택배사는 필수입니다.")
        @Size(max = 30, message = "택배사는 30자 이하여야 합니다.")
        String courierCompany
) {
    public RegisterTrackingNumberCommand toCommand(UUID sellerId, UUID orderId) {
        return new RegisterTrackingNumberCommand(
                sellerId,
                orderId,
                this.trackingNumber,
                this.courierCompany
        );
    }
}
