package dev.magadiflo.accounts.app.exception.model;

public class AccountNotFoundException extends RuntimeException {
    private static final String MESSAGE = "El número de cuenta [%s] no se encuentra";

    public AccountNotFoundException(String accountNumber) {
        super(MESSAGE.formatted(accountNumber));
    }
}
