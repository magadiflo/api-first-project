package dev.magadiflo.accounts.app.exception;

import dev.magadiflo.accounts.app.exception.model.AccountNotFoundException;
import dev.magadiflo.accounts.app.exception.model.BusinessRuleException;
import dev.magadiflo.accounts.app.exception.model.InvalidStatusTransitionException;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class AccountErrors {
    public static <T> Mono<T> accountNotFound(String accountNumber) {
        return Mono.error(() -> new AccountNotFoundException(accountNumber));
    }

    public static <T> Mono<T> invalidStatusTransition(String status) {
        return Mono.error(() -> new InvalidStatusTransitionException(status));
    }

    public static <T> Mono<T> maxAccountsReached(String customerId) {
        return Mono.error(() ->
                new BusinessRuleException("El cliente [%s] ya tiene el máximo de cuentas permitidas"
                        .formatted(customerId))
        );
    }

    public static <T> Mono<T> cannotModifyAliasClosedAccount() {
        return Mono.error(() ->
                new BusinessRuleException("No se permite modificar el alias de una cuenta en estado CLOSED")
        );
    }

    public static <T> Mono<T> cannotActivateClosedAccount() {
        return Mono.error(() ->
                new BusinessRuleException("No se puede activar una cuenta que está CLOSED")
        );
    }
}
