package com.ysh.back.common.exception.handler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public HttpEntity<?> handleBadRequestException(Exception exception){

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(1)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.OK
        );
    }
}
