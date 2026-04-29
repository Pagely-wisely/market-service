package com.pagely.marketservice.domain.model;

import com.pagely.common.entity.BaseEntity;
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
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 주문 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 이전 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false, length = 20)
    private OrderStatus fromStatus;

    // 변경 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 20)
    private OrderStatus toStatus;

    // 변경 사유
    @Column(length = 200)
    private String reason;

    public static OrderHistory of(Order order, OrderStatus fromStatus, String reason) {
        OrderHistory history = new OrderHistory();
        history.order = order;
        history.fromStatus = fromStatus;
        history.toStatus = order.getStatus();
        history.reason = reason;
        return history;
    }
}
