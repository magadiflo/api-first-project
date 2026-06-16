package dev.magadiflo.accounts.app.exception.response;

public record ValidationError(String field,
                              String message) {
}
