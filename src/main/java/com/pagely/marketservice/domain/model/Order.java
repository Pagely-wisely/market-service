package com.pagely.marketservice.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 구매자 ID
    @Column(name = "buyer_id", nullable = false, columnDefinition = "uuid")
    private UUID buyerId;

    // 판매글 ID
    @Column(name = "sale_post_id", nullable = false, columnDefinition = "uuid")
    private UUID salePostId;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    // 가격 (결제 시점 스냅샷)
    @Column(nullable = false)
    private Integer price;

    // 운송장 번호
    @Column(name = "tracking_number", length = 50)
    private String trackingNumber;

    // 택배사
    @Column(name = "courier_company", length = 50)
    private String courierCompany;

    // 운송장 등록 일시
    @Column(name = "tracking_registered_at")
    private LocalDateTime trackingRegisteredAt;

    // 공통 감사 컬럼
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, columnDefinition = "uuid")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", columnDefinition = "uuid")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", columnDefinition = "uuid")
    private UUID deletedBy;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistory> histories = new ArrayList<>();
}
