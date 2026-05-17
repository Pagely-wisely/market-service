package com.pagely.marketservice.presentation.dto.response;

import com.pagely.marketservice.application.dto.result.SalePostResult;
import com.pagely.marketservice.domain.model.SalePostStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record SalePostSummaryResponse(
        UUID id,                // 판매글ID
        String title,           // 판매글 제목
        int price,              // 판매 가격
        SalePostStatus status,  // 판매글 상태
        LocalDateTime createdAt // 생성 일시
) {
    public static SalePostSummaryResponse fromResult(SalePostResult result) {
        return new SalePostSummaryResponse(
                result.id(),
                result.title(),
                result.price(),
                result.status(),
                result.createdAt()
        );
    }
}
