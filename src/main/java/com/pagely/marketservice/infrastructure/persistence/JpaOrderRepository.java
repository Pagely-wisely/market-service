package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
}
