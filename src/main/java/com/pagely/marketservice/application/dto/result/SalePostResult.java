package com.pagely.marketservice.application.dto.result;

import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.model.SalePostStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record SalePostResult(
        UUID id,                // 판매글ID
        UUID sellerId,          // 판매자ID
        String bookId,          // 판매도서ID
        String title,           // 판매글 제목
        String description,     // 판매글 상세 설명
        int price,              // 판매 가격
        SalePostStatus status,  // 판매글 상태
        String condition,       // 상품 상태
        LocalDateTime createdAt,// 생성 일시
        UUID createdBy,         // 생성자ID
        LocalDateTime updatedAt,// 수정 일시
        UUID updatedBy          // 수정자ID
) {
    public static SalePostResult fromEntity(SalePost salePost) {
        return new SalePostResult(
                salePost.getId(),
                salePost.getSellerId(),
                salePost.getBookId(),
                salePost.getTitle(),
                salePost.getDescription(),
                salePost.getPrice(),
                salePost.getStatus(),
                salePost.getCondition(),
                salePost.getCreatedAt(),
                salePost.getCreatedBy(),
                salePost.getUpdatedAt(),
                salePost.getUpdatedBy()
        );
    }
}
