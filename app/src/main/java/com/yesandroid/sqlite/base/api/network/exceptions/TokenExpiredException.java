package com.yesandroid.sqlite.base.api.network.exceptions;

/**
 * Custom class to uniquely Token Expiry
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
