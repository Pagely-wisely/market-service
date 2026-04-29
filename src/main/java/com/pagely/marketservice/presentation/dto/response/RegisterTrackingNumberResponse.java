package com.pagely.marketservice.presentation.dto.response;

import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.domain.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterTrackingNumberResponse(
        UUID id,                            // 주문 ID
        UUID buyerId,                       // 구매자 ID
        UUID salePostId,                    // 판매글 ID
        OrderStatus status,                 // 주문 상태
        String trackingNumber,              // 운송장 번호
        String courierCompany,              // 택배사
        LocalDateTime trackingRegisteredAt  // 운송장 등록 일시
) {
    public static RegisterTrackingNumberResponse fromResult(OrderResult r) {
        return new RegisterTrackingNumberResponse(
                r.id(),
                r.buyerId(),
                r.salePostId(),
                r.status(),
                r.trackingNumber(),
                r.courierCompany(),
                r.trackingRegisteredAt()
        );
    }
}
