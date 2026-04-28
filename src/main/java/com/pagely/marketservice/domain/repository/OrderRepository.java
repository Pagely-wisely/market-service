package com.pagely.marketservice.domain.repository;

import com.pagely.marketservice.domain.model.Order;

public interface OrderRepository {
    Order save(Order order);
}
