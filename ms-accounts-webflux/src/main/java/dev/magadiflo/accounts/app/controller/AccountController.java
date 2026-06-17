package dev.magadiflo.accounts.app.controller;

import dev.magadiflo.accounts.app.api.AccountsApi;
import dev.magadiflo.accounts.app.domain.service.AccountService;
import dev.magadiflo.accounts.app.model.AccountPageResponse;
import dev.magadiflo.accounts.app.model.AccountRequest;
import dev.magadiflo.accounts.app.model.AccountResponse;
import dev.magadiflo.accounts.app.model.AccountStatus;
import dev.magadiflo.accounts.app.model.AccountStatusRequest;
import dev.magadiflo.accounts.app.model.AccountUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/{version}", version = "1")
public class AccountController implements AccountsApi {

    private final AccountService accountService;

    @Override
    public Mono<ResponseEntity<AccountResponse>> createAccount(Mono<AccountRequest> accountRequest, ServerWebExchange exchange) {
        return accountRequest
                .flatMap(this.accountService::createAccount)
                .map(accountResponse ->
                        ResponseEntity.status(HttpStatus.CREATED).body(accountResponse));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteAccount(String accountNumber, ServerWebExchange exchange) {
        return this.accountService.deleteAccount(accountNumber)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> getAccountByNumber(String accountNumber, ServerWebExchange exchange) {
        return this.accountService.getAccount(accountNumber)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountPageResponse>> listAccounts(String customerId, AccountStatus status, Integer page,
                                                                  Integer size, ServerWebExchange exchange) {
        return this.accountService.getAccounts(customerId, status, page, size)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> updateAccount(String accountNumber,
                                                               Mono<AccountUpdateRequest> accountUpdateRequest,
                                                               ServerWebExchange exchange) {
        return accountUpdateRequest
                .flatMap(request -> this.accountService.updateAccount(accountNumber, request))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AccountResponse>> updateAccountStatus(String accountNumber,
                                                                     Mono<AccountStatusRequest> accountStatusRequest,
                                                                     ServerWebExchange exchange) {
        return accountStatusRequest
                .flatMap(request -> this.accountService.updateAccountStatus(accountNumber, request))
                .map(ResponseEntity::ok);
    }
}
