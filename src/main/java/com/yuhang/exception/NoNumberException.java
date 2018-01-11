package com.yuhang.exception;

/**
 *  库存不足
 */
public class NoNumberException extends RuntimeException {
    public NoNumberException(String message) {
        super(message);
    }

    public NoNumberException(String message,Throwable cause) {
        super(message,cause);
    }
}
