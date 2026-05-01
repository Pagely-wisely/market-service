package com.pagely.marketservice.application.service;

import com.pagely.common.exception.BusinessException;
import com.pagely.marketservice.application.dto.command.CreateOrderCommand;
import com.pagely.marketservice.application.dto.command.RegisterTrackingNumberCommand;
import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.domain.event.OrderEvents;
import com.pagely.marketservice.domain.event.payload.OrderCancelledEvent;
import com.pagely.marketservice.domain.event.payload.OrderCreatedEvent;
import com.pagely.marketservice.domain.exception.OrderErrorCode;
import com.pagely.marketservice.domain.exception.SalePostErrorCode;
import com.pagely.marketservice.domain.model.Order;
import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.repository.OrderRepository;
import com.pagely.marketservice.domain.repository.SalePostRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final SalePostRepository salePostRepository;
    private final OrderEvents orderEvents;

    @Transactional
    public OrderResult createOrder(CreateOrderCommand command) {

        SalePost salePost = salePostRepository.findById(command.salePostId())
                .orElseThrow(() -> new BusinessException(SalePostErrorCode.SALE_POST_NOT_FOUND));

        Order order = Order.create(command.buyerId(), salePost);

        Order saved = orderRepository.save(order);

        //TODO: 주문 생성 완료 이벤트 발행 처리
        orderEvents.orderCreated(OrderCreatedEvent.of(saved));

        return OrderResult.fromEntity(saved);
    }

    public OrderResult getOrder(UUID buyerId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        order.validateBuyer(buyerId); // 구매자의 주문인지 검증

        return OrderResult.fromEntity(order);
    }

    // 판매자가 운송장 번호 등록
    @Transactional
    public OrderResult registerTrackingNumber(RegisterTrackingNumberCommand command) {
        // 주문 조회
        Order order = orderRepository.findByIdWithSalePost(command.orderId())
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));
        // 주문의 판매자인지 검증
        order.validateSeller(command.sellerId());

        // 운송장 번호 등록
        order.registerTrackingNumber(command.trackingNumber(), command.courierCompany());

        return OrderResult.fromEntity(order);
    }

    // 구매자 구매 확정
    @Transactional
    public OrderResult confirmOrder(UUID buyerId, UUID orderId) {
        // 주문 조회
        Order order = orderRepository.findByIdWithSalePost(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));
        // 구매자인지 검증
        order.validateBuyer(buyerId);

        // 구매 확정
        order.confirm();

        return OrderResult.fromEntity(order);
    }

    // 구매자 주문 취소
    @Transactional
    public OrderResult cancelOrder(UUID buyerId, UUID orderId) {
        Order order = orderRepository.findByIdWithSalePost(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));
        order.validateBuyer(buyerId);

        boolean needsRefund = order.isAccepted();

        order.cancel();

        if (needsRefund) {
            orderEvents.orderCancelled(OrderCancelledEvent.of(order)); // 결제 취소 이벤트
        }

        return OrderResult.fromEntity(order);
    }
}
