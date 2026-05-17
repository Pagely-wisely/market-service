package com.pagely.marketservice.domain.exception;

import com.pagely.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SalePostErrorCode implements ErrorCode {
    SALE_POST_CANNOT_RESTORE("판매글을 판매 가능 상태로 되돌릴 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_AVAILABLE("해당 판매글은 주문이 불가능한 상태입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_ORDER_OWN("본인이 등록한 판매글은 주문할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_SALE_PRICE("판매 가격은 0보다 커야합니다.", HttpStatus.BAD_REQUEST),
    SALE_POST_NOT_FOUND("해당 판매글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    SalePostErrorCode(String message, HttpStatus httpStatus) {
        this.code = this.name();
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
