package com.wooteco.haveagoodtime.exception;

import com.wooteco.haveagoodtime.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HaveagoodtimeException.class)
    public ResponseEntity<ErrorResponse> handle(HaveagoodtimeException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
    }
}
