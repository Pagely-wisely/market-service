package com.pagely.marketservice.presentation.controller;

import com.pagely.common.pagination.PageRequest;
import com.pagely.common.pagination.PageResponse;
import com.pagely.common.response.ApiResponse;
import com.pagely.marketservice.application.dto.command.CreateSalePostCommand;
import com.pagely.marketservice.application.dto.result.SalePostResult;
import com.pagely.marketservice.application.service.SalePostService;
import com.pagely.marketservice.presentation.dto.request.CreateSalePostRequest;
import com.pagely.marketservice.presentation.dto.response.CreateSalePostResponse;
import com.pagely.marketservice.presentation.dto.response.GetSalePostResponse;
import com.pagely.marketservice.presentation.dto.response.SalePostSummaryResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sale-posts")
public class SalePostController {

    private final SalePostService salePostService;

    @PostMapping()
    public ResponseEntity<ApiResponse> createSalePost(
            @RequestHeader("X-User-Id") UUID sellerId,
            @Valid @RequestBody CreateSalePostRequest request
    ) {
        CreateSalePostCommand command = request.toCommand(sellerId);
        SalePostResult result = salePostService.createSalePost(command);
        CreateSalePostResponse response = CreateSalePostResponse.fromResult(result);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSalePosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size).toPageable();
        Page<SalePostResult> results = salePostService.getSalePosts(pageable);
        return ApiResponse.ok(PageResponse.of(results, SalePostSummaryResponse::fromResult));
    }

    @GetMapping("/{salePostId}")
    public ResponseEntity<ApiResponse> getSalePost(
            @PathVariable("salePostId") UUID salePostId
    ) {
        SalePostResult result = salePostService.getSalePost(salePostId);
        return ApiResponse.ok(GetSalePostResponse.fromResult(result));
    }
}
