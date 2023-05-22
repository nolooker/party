package com.party.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 어노테이션을 사용하여 컨트롤러에서 전역적으로 예외를 처리하는 것입니다.

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // 예외 처리 로직 구현
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 무결성 제약조건을 위반했습니다.");
    }

}