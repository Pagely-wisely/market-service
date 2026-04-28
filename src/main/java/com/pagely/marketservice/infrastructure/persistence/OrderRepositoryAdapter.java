package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.Order;
import com.pagely.marketservice.domain.repository.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpaOrderRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findByIdWithSalePost(UUID orderId) {
        return jpaOrderRepository.findByIdWithSalePost(orderId);
    }
}
