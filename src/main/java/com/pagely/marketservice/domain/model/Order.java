package com.pagely.marketservice.domain.model;

import com.pagely.common.entity.BaseEntity;
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
}
