package dev.magadiflo.accounts.app.domain.service.impl;

import dev.magadiflo.accounts.app.domain.entity.Account;
import dev.magadiflo.accounts.app.domain.repository.AccountRepository;
import dev.magadiflo.accounts.app.domain.service.AccountService;
import dev.magadiflo.accounts.app.exception.factory.AccountErrors;
import dev.magadiflo.accounts.app.mapper.AccountMapper;
import dev.magadiflo.accounts.app.model.AccountPageResponse;
import dev.magadiflo.accounts.app.model.AccountRequest;
import dev.magadiflo.accounts.app.model.AccountResponse;
import dev.magadiflo.accounts.app.model.AccountStatus;
import dev.magadiflo.accounts.app.model.AccountStatusRequest;
import dev.magadiflo.accounts.app.model.AccountUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Mono<AccountPageResponse> getAccounts(String customerId, AccountStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String statusValue = Objects.nonNull(status) ? status.getValue() : null;

        Flux<Account> accountFlux;
        Mono<Long> totalMono;

        if (Objects.nonNull(customerId) && Objects.nonNull(statusValue)) {
            accountFlux = this.accountRepository.findAllByCustomerIdAndStatus(customerId, statusValue, pageable);
            totalMono = this.accountRepository.countByCustomerIdAndStatus(customerId, statusValue);
            return this.buildPageResponse(accountFlux, totalMono, pageable);
        }

        if (Objects.nonNull(customerId)) {
            accountFlux = this.accountRepository.findAllByCustomerId(customerId, pageable);
            totalMono = this.accountRepository.countByCustomerId(customerId);
            return this.buildPageResponse(accountFlux, totalMono, pageable);
        }

        if (Objects.nonNull(statusValue)) {
            accountFlux = this.accountRepository.findAllByStatus(statusValue, pageable);
            totalMono = this.accountRepository.countByStatus(statusValue);
            return this.buildPageResponse(accountFlux, totalMono, pageable);
        }

        accountFlux = this.accountRepository.findAllBy(pageable);
        totalMono = this.accountRepository.count();
        return this.buildPageResponse(accountFlux, totalMono, pageable);
    }

    @Override
    public Mono<AccountResponse> getAccount(String accountNumber) {
        return this.findAccountOrThrow(accountNumber)
                .map(this.accountMapper::toAccountResponse);
    }

    @Override
    @Transactional
    public Mono<AccountResponse> createAccount(AccountRequest request) {
        return this.accountRepository.countByCustomerId(request.getCustomerId())
                // Regla de negocio: máximo 5 cuentas por cliente
                .filter(count -> count < 5)
                .switchIfEmpty(AccountErrors.maxAccountsReached(request.getCustomerId()))
                .map(count -> {
                    Account account = this.accountMapper.toAccount(request);
                    account.setAccountNumber(this.generateAccountNumber());
                    return account;
                })
                .flatMap(this.accountRepository::save)
                .map(this.accountMapper::toAccountResponse);
    }

    @Override
    @Transactional
    public Mono<AccountResponse> updateAccount(String accountNumber, AccountUpdateRequest request) {
        return this.findAccountOrThrow(accountNumber)
                // Regla de negocio: no se puede modificar una cuenta CLOSED
                .filter(account -> !AccountStatus.CLOSED.getValue().equals(account.getStatus()))
                .switchIfEmpty(AccountErrors.cannotModifyAliasClosedAccount())
                .map(account -> this.accountMapper.toUpdateAccount(account, request))
                .flatMap(this.accountRepository::save)
                .map(this.accountMapper::toAccountResponse);
    }

    @Override
    @Transactional
    public Mono<AccountResponse> updateAccountStatus(String accountNumber, AccountStatusRequest request) {
        return this.findAccountOrThrow(accountNumber)
                // 1️⃣ Primero valida que no sea la misma transición (409)
                .filter(account -> !account.getStatus().equals(request.getStatus().getValue()))
                .switchIfEmpty(AccountErrors.invalidStatusTransition(request.getStatus().getValue()))
                // 2️⃣ Luego valida que la cuenta no esté CLOSED (422)
                .filter(account -> !AccountStatus.CLOSED.getValue().equals(account.getStatus()))
                .switchIfEmpty(AccountErrors.cannotActivateClosedAccount())
                .map(account -> {
                    account.setStatus(request.getStatus().getValue());
                    return account;
                })
                .flatMap(this.accountRepository::save)
                .map(this.accountMapper::toAccountResponse);
    }

    @Override
    @Transactional
    public Mono<Void> deleteAccount(String accountNumber) {
        return this.findAccountOrThrow(accountNumber)
                .map(account -> {
                    account.setStatus(AccountStatus.CLOSED.getValue());
                    return account;
                })
                .flatMap(this.accountRepository::save)
                .then();
    }

    private Mono<Account> findAccountOrThrow(String accountNumber) {
        return this.accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(AccountErrors.accountNotFound(accountNumber));
    }

    private Mono<AccountPageResponse> buildPageResponse(Flux<Account> accountFlux, Mono<Long> totalMono, Pageable pageable) {
        return accountFlux
                .map(this.accountMapper::toAccountResponse)
                .collectList()
                .zipWith(
                        totalMono,
                        (accountResponseList, total) ->
                                new PageImpl<>(accountResponseList, pageable, total)
                )
                .map(this.accountMapper::toAccountPageResponse);
    }

    private String generateAccountNumber() {
        // Genera un número aleatorio de cuenta de 20 dígitos
        // En producción esto vendría de un servicio de numeración bancaria
        return "002191002300" + String.format("%08d", (int) (Math.random() * 100_000_000));
    }
}
