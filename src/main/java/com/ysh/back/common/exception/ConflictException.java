package com.ysh.back.common.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        super("중복되는 데이터입니다.");
    }
}
