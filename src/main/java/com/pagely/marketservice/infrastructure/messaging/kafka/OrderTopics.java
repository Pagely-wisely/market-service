package com.pagely.marketservice.infrastructure.messaging.kafka;

public final class OrderTopics {

    private OrderTopics() {}

    public static final String ORDER_CREATED = "order-created";
    public static final String ORDER_CANCELLED = "order-cancelled";
}