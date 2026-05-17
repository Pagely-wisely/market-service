package com.pagely.marketservice.presentation.controller;

import com.pagely.common.auth.Role;
import com.pagely.common.auth.annotation.AuthRequired;
import com.pagely.common.auth.annotation.CurrentUserId;
import com.pagely.common.response.ApiResponse;
import com.pagely.marketservice.application.dto.result.OrderResult;
import com.pagely.marketservice.application.service.OrderService;
import com.pagely.marketservice.presentation.dto.request.CreateOrderRequest;
import com.pagely.marketservice.presentation.dto.request.RegisterTrackingNumberRequest;
import com.pagely.marketservice.presentation.dto.response.CancelOrderResponse;
import com.pagely.marketservice.presentation.dto.response.CreateOrderResponse;
import com.pagely.marketservice.presentation.dto.response.OrderResponse;
import com.pagely.marketservice.presentation.dto.response.RegisterTrackingNumberResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> createOrder(
            @CurrentUserId UUID buyerId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResult result = orderService.createOrder(request.toCommand(buyerId));
        return ApiResponse.ok(CreateOrderResponse.fromResult(result));
    }

    @GetMapping("/{orderId}")
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> getOrder(
            @CurrentUserId UUID buyerId,
            @PathVariable("orderId") UUID orderId
    ) {
        OrderResult result = orderService.getOrder(buyerId, orderId);
        return ApiResponse.ok(OrderResponse.fromResult(result));
    }

    @PostMapping("/{orderId}/tracking")
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> registerTrackingNumber(
            @CurrentUserId UUID sellerId,
            @PathVariable("orderId") UUID orderId,
            @Valid @RequestBody RegisterTrackingNumberRequest request
    ) {
        OrderResult result = orderService.registerTrackingNumber(request.toCommand(sellerId, orderId));
        return ApiResponse.ok(RegisterTrackingNumberResponse.fromResult(result));
    }

    @PostMapping("/{orderId}/confirm")
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> confirmOrder(
            @CurrentUserId UUID buyerId,
            @PathVariable("orderId") UUID orderId
    ) {
        OrderResult result = orderService.confirmOrder(buyerId, orderId);
        return ApiResponse.ok(OrderResponse.fromResult(result));
    }

    @PostMapping("/{orderId}/cancel")
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> cancelOrder(
            @CurrentUserId UUID buyerId,
            @PathVariable("orderId") UUID orderId
    ) {
        OrderResult result = orderService.cancelOrder(buyerId, orderId);
        return ApiResponse.ok(CancelOrderResponse.fromResult(result));
    }
}
