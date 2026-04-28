package com.pagely.marketservice.domain.repository;

import com.pagely.marketservice.domain.model.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Optional<Order> findByIdWithSalePost(UUID orderID);
}
