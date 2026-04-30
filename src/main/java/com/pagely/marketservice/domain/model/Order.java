package com.pagely.marketservice.domain.model;

import com.pagely.common.entity.BaseEntity;
import com.pagely.common.exception.BusinessException;
import com.pagely.marketservice.domain.exception.OrderErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 구매자 ID
    @Column(name = "buyer_id", nullable = false, columnDefinition = "uuid")
    private UUID buyerId;

    // 판매글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_post_id", nullable = false)
    private SalePost salePost;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    // 가격 (결제 시점 스냅샷)
    @Column(nullable = false)
    private int price;

    // 운송장 번호
    @Column(name = "tracking_number", length = 50)
    private String trackingNumber;

    // 택배사
    @Column(name = "courier_company", length = 50)
    private String courierCompany;

    // 운송장 등록 일시
    @Column(name = "tracking_registered_at")
    private LocalDateTime trackingRegisteredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistory> histories = new ArrayList<>();

    public static Order create(
            UUID buyerId,
            SalePost salePost
    ) {
        salePost.validateOrderable(buyerId);

        Order order = new Order();
        order.buyerId = buyerId;
        order.salePost = salePost;
        order.status = OrderStatus.PENDING;
        order.price = salePost.getPrice();

        salePost.markReserved(); // 주문 생성 시 판매글 예약상태로 변경

        return order;
    }

    public void validateBuyer(UUID buyerId) {
        if (!this.buyerId.equals(buyerId)) {
            throw new BusinessException(OrderErrorCode.ORDER_BUYER_MISMATCH);
        }
    }

    // 해당 주문의 판매자인지 검증
    public void validateSeller(UUID sellerId) {
        if (!this.getSalePost().isSeller(sellerId)) {
            throw new BusinessException(OrderErrorCode.ORDER_SELLER_MISMATCH);
        }
    }

    // 운송장 번호 / 택배사 정보 등록
    public void registerTrackingNumber(String trackingNumber, String courierCompany) {
        // 주문 승인(ACCEPTED) 상태에서만 운송장 정보 등록 가능
        if (this.status != OrderStatus.ACCEPTED) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_NOT_ACCEPTED);
        }

        this.trackingNumber = trackingNumber;
        this.courierCompany = courierCompany;
        this.trackingRegisteredAt = LocalDateTime.now();
        OrderStatus prevStatus = this.status;
        this.status = OrderStatus.SHIPPING;

        // 주문 이력 생성
        this.histories.add(OrderHistory.of(this, prevStatus, "운송장 등록"));
    }

    // 구매 확정 (구매자)
    public void confirm() {
        // 상태값
        if (this.status != OrderStatus.SHIPPING) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_NOT_SHIPPING);
        }

        OrderStatus prevStatus = this.status;

        // 상태변경
        this.status = OrderStatus.COMPLETED;

        // 주문 이력 생성
        this.histories.add(OrderHistory.of(this, prevStatus, "구매 확정"));
    }

    // 주문 취소
    // PENDING: 결제 전 취소 → 이벤트 불필요
    // ACCEPTED: 결제 완료 후 취소 → 결제 취소 이벤트 필요 (서비스 레이어에서 처리)
    public void cancel() {
        if (this.status != OrderStatus.PENDING && this.status != OrderStatus.ACCEPTED) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_CANCELLABLE);
        }

        OrderStatus prevStatus = this.status;
        this.status = OrderStatus.CANCELLED;
        this.salePost.markAvailable();
        
        this.histories.add(OrderHistory.of(this, prevStatus, "주문 취소")); // 주문 이력 생성
    }

    public boolean isAccepted() {
        return this.status == OrderStatus.ACCEPTED;
    }
}
