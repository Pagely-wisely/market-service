package com.pagely.marketservice.presentation.dto.response;

import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.domain.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,                            // 주문 ID
        UUID buyerId,                       // 구매자 ID
        UUID salePostId,                    // 판매글 ID
        OrderStatus status,                 // 주문 상태
        int price,                          // 가격
        String trackingNumber,              // 운송장 번호
        String courierCompany,              // 택배사
        LocalDateTime trackingRegisteredAt, // 운송장 등록 일시
        LocalDateTime createdAt,            // 생성 시각
        LocalDateTime updatedAt             // 수정 시각
) {
    public static OrderResponse fromResult(OrderResult r) {
        return new OrderResponse(
                r.id(),
                r.buyerId(),
                r.salePostId(),
                r.status(),
                r.price(),
                r.trackingNumber(),
                r.courierCompany(),
                r.trackingRegisteredAt(),
                r.createdAt(),
                r.updatedAt()
        );
    }
}
