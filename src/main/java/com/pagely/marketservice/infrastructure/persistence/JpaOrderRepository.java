package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
            	select o from Order o
            	left join fetch o.salePost sp
            	where o.id = :orderId
            	and o.deletedAt is null
            """)
    Optional<Order> findByIdWithSalePost(@Param("orderId") UUID orderId);
}
