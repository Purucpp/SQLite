package com.yesandroid.sqlite.base.api.network.exceptions;

/**
 * Custom class to uniquely identify Network Failure
 */
public class NetworkFailureException extends Exception {

    public NetworkFailureException(String message) {
        super(message);
    }
}
