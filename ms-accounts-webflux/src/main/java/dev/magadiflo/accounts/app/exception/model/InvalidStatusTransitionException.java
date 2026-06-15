package dev.magadiflo.accounts.app.exception.model;

public class InvalidStatusTransitionException extends RuntimeException {
    private static final String MESSAGE = "La cuenta ya se encuentra en estado [%s]";

    public InvalidStatusTransitionException(String status) {
        super(MESSAGE.formatted(status));
    }
}
