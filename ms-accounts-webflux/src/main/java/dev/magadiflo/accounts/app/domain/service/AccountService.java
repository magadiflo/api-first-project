package dev.magadiflo.accounts.app.domain.service;

import dev.magadiflo.accounts.app.model.AccountPageResponse;
import dev.magadiflo.accounts.app.model.AccountRequest;
import dev.magadiflo.accounts.app.model.AccountResponse;
import dev.magadiflo.accounts.app.model.AccountStatus;
import dev.magadiflo.accounts.app.model.AccountStatusRequest;
import dev.magadiflo.accounts.app.model.AccountUpdateRequest;

import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<AccountPageResponse> getAccounts(String customerId, AccountStatus status, int pageNumber, int pageSize);

    Mono<AccountResponse> getAccount(String accountNumber);

    Mono<AccountResponse> createAccount(AccountRequest request);

    Mono<AccountResponse> updateAccount(String accountNumber, AccountUpdateRequest request);

    Mono<AccountResponse> updateAccountStatus(String accountNumber, AccountStatusRequest request);

    Mono<Void> deleteAccount(String accountNumber);
}
