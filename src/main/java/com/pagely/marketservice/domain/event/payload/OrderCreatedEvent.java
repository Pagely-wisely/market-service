package com.pagely.marketservice.domain.event.payload;

import com.pagely.marketservice.domain.event.BaseEvent;
import com.pagely.marketservice.domain.model.Order;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderCreatedEvent extends BaseEvent {

    private static final String DOMAIN_TYPE = "ORDER";

    private OrderCreatedEvent(UUID orderId, Object payload) {
        super(DOMAIN_TYPE, orderId, payload);
    }

    public static OrderCreatedEvent of(Order order, UUID sellerId) {
        return new OrderCreatedEvent(
                order.getId(),
                new Payload(
                        order.getId(),
                        order.getBuyerId(),
                        order.getPrice(),
                        sellerId
                )
        );
    }

    public record Payload(UUID orderId, UUID buyerId, int price, UUID sellerId) {
    }
}
