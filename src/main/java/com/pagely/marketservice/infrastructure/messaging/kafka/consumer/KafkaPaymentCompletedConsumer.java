package com.pagely.marketservice.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.marketservice.application.dto.command.AcceptOrderCommand;
import com.pagely.marketservice.application.service.OrderService;
import com.pagely.marketservice.infrastructure.messaging.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentCompletedConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payment-completed",
            groupId = "market-service"
    )
    public void handleOrderCreated(String strEvent) {
        try {
            PaymentCompletedEvent event = objectMapper.readValue(strEvent, PaymentCompletedEvent.class);
            log.info("[payment] payment-completed 이벤트 수신: {}", event.domainId());

            AcceptOrderCommand command = new AcceptOrderCommand(event.payload().orderId());
            
            orderService.acceptOrder(command);
        } catch (JsonProcessingException e) {
            log.error("[market] payment-completed 이벤트 역직렬화 실패. message={}", strEvent, e);
            //TODO: 실패 재처리 로직 필요
        } catch (Exception e) {
            log.error("[market] payment-completed 처리 실패. message={}", strEvent, e);
            //TODO: 실패 재처리 로직 필요
        }
    }
}
