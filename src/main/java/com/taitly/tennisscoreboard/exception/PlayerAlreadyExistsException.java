package com.taitly.tennisscoreboard.exception;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}