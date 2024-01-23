package com.sportsecho.purchase.exception;

import com.sportsecho.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PurchaseErrorCode implements BaseErrorCode {

    EMPTY_CART(HttpStatus.BAD_REQUEST, "장바구니가 비어있습니다."),
    NOT_FOUND_PURCHASE(HttpStatus.BAD_REQUEST, "구매 내역이 없습니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
