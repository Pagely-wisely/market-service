package com.pagely.marketservice.domain.event.payload;

import com.pagely.marketservice.domain.event.BaseEvent;
import com.pagely.marketservice.domain.model.Order;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderCancelledEvent extends BaseEvent {

    private static final String DOMAIN_TYPE = "ORDER";

    private OrderCancelledEvent(UUID orderId, Object payload) {
        super(DOMAIN_TYPE, orderId, payload);
    }

    public static OrderCancelledEvent of(Order order) {
        return new OrderCancelledEvent(
                order.getId(),
                new Payload(
                        order.getId(),
                        order.getPrice()
                )
        );
    }

    public record Payload(UUID orderId, int price) {
    }
}
