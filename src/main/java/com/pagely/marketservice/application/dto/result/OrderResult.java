package com.pagely.marketservice.application.dto.result;

import com.pagely.marketservice.domain.model.Order;
import com.pagely.marketservice.domain.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResult(
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
    public static OrderResult fromEntity(Order o) {
        return new OrderResult(
                o.getId(),
                o.getBuyerId(),
                o.getSalePost().getId(),
                o.getStatus(),
                o.getPrice(),
                o.getTrackingNumber(),
                o.getCourierCompany(),
                o.getTrackingRegisteredAt(),
                o.getCreatedAt(),
                o.getUpdatedAt()
        );
    }
}
