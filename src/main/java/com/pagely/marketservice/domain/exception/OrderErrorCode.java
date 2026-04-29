package com.pagely.marketservice.domain.exception;

import com.pagely.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderErrorCode implements ErrorCode {
    NOT_ORDER_OWNER("본인의 주문만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),
    ORDER_SELLER_MISMATCH("해당 주문의 판매자가 아닙니다.", HttpStatus.FORBIDDEN),
    ORDER_STATUS_NOT_ACCEPTED("주문 승인 상태에서만 가능합니다.", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("해당 주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderErrorCode(String message, HttpStatus httpStatus) {
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
