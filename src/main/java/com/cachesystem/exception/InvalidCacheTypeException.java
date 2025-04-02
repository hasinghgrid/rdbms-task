package com.cachesystem.exception;

public class InvalidCacheTypeException extends RuntimeException {
    public InvalidCacheTypeException(String message) {
        super(message);
    }
}
