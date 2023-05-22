package com.party.exception;

// 재고 없을 때 예외 발생 (우리는 시간으로 재고)
public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message) {
        super(message) ;
    }
}
