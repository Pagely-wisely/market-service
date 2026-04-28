package com.pagely.marketservice.presentation.controller;

import com.pagely.common.response.ApiResponse;
import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.application.service.OrderService;
import com.pagely.marketservice.presentation.dto.request.CreateOrderRequest;
import com.pagely.marketservice.presentation.dto.response.CreateOrderResponse;
import com.pagely.marketservice.presentation.dto.response.OrderResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(
            @RequestHeader("X-User-Id") UUID buyerId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResult result = orderService.createOrder(request.toCommand(buyerId));
        return ApiResponse.ok(CreateOrderResponse.fromResult(result));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(
            @PathVariable("orderId") UUID orderId
    ) {
        OrderResult result = orderService.getOrder(orderId);
        return ApiResponse.ok(OrderResponse.fromResult(result));
    }
}
