package com.pagely.marketservice.presentation.dto.response;

import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.domain.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderResponse(
        UUID id,                            // 주문 ID
        UUID buyerId,                       // 구매자 ID
        UUID salePostId,                    // 판매글 ID
        OrderStatus status,                 // 주문 상태
        int price,                          // 가격
        LocalDateTime createdAt,            // 생성 시각
        UUID createdBy                      // 생성자
) {
    public static CreateOrderResponse fromResult(OrderResult r) {
        return new CreateOrderResponse(
                r.id(),
                r.buyerId(),
                r.salePostId(),
                r.status(),
                r.price(),
                r.createdAt(),
                r.createdBy()
        );
    }
}
