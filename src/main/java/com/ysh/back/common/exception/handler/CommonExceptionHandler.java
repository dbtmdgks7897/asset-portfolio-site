package com.ysh.back.common.exception.handler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ysh.back.common.dto.ResponseDTO;
import com.ysh.back.common.exception.BadRequestException;
import com.ysh.back.common.exception.ConflictException;
import com.ysh.back.common.exception.LoginException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public HttpEntity<?> handleBadRequestException(Exception exception){

        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(1)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(LoginException.class)
    public HttpEntity<?> handleLoginException(Exception exception){
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(1)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ConflictException.class)
    public HttpEntity<?> handleConflictException(Exception exception){
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .code(1)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }
}
