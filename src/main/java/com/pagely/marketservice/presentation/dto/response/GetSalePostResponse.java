package com.pagely.marketservice.presentation.dto.response;

import com.pagely.marketservice.application.dto.result.SalePostResult;
import com.pagely.marketservice.domain.model.SalePostStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetSalePostResponse(
        UUID id,                // 판매글ID
        UUID sellerId,          // 판매자ID
        String bookId,          // 판매도서ID
        String title,           // 판매글 제목
        String description,     // 판매글 상세 설명
        int price,              // 판매 가격
        SalePostStatus status,  // 판매글 상태
        String condition,       // 상품 상태
        LocalDateTime createdAt,// 생성 일시
        UUID createdBy          // 생성자ID
) {
    public static GetSalePostResponse fromResult(SalePostResult result) {
        return new GetSalePostResponse(
                result.id(),
                result.sellerId(),
                result.bookId(),
                result.title(),
                result.description(),
                result.price(),
                result.status(),
                result.condition(),
                result.createdAt(),
                result.createdBy()
        );
    }
}
