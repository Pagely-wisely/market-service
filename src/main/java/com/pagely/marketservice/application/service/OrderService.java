package com.pagely.marketservice.application.service;

import com.pagely.common.exception.BusinessException;
import com.pagely.marketservice.application.dto.command.CreateOrderCommand;
import com.pagely.marketservice.application.dto.result.OrderResult;
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

    @Transactional
    public OrderResult createOrder(CreateOrderCommand command) {

        SalePost salePost = salePostRepository.findById(command.salePostId())
                .orElseThrow(() -> new BusinessException(SalePostErrorCode.SALE_POST_NOT_FOUND));

        Order order = Order.create(command.buyerId(), salePost);

        Order saved = orderRepository.save(order);

        //TODO: 주문 생성 완료 이벤트 발행 처리

        return OrderResult.fromEntity(saved);
    }

    public OrderResult getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));
        return OrderResult.fromEntity(order);
    }
}
