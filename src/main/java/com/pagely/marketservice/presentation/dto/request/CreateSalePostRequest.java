package com.pagely.marketservice.presentation.dto.request;

import com.pagely.marketservice.application.dto.command.CreateSalePostCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateSalePostRequest(
        @NotBlank(message = "bookId는 필수입니다.")
        String bookId,

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "설명은 필수입니다.")
        String description,

        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        @Max(value = 10_000_000, message = "가격은 1000만원 이하여야 합니다.")
        int price,

        @NotBlank(message = "도서 상태는 필수입니다.")
        @Size(max = 20, message = "도서 상태는 20자 이하여야 합니다.")
        String condition
) {
    public CreateSalePostCommand toCommand(UUID sellerId) {
        return new CreateSalePostCommand(
                sellerId,
                this.bookId(),
                this.title(),
                this.description(),
                this.price(),
                this.condition()
        );
    }
}
