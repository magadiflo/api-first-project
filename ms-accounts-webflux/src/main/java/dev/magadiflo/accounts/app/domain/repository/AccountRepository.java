package dev.magadiflo.accounts.app.domain.repository;

import dev.magadiflo.accounts.app.domain.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
    Mono<Boolean> existsByAccountNumber(String accountNumber);

    Mono<Account> findByAccountNumber(String accountNumber);

    Mono<Long> countByCustomerId(String customerId);

    Mono<Long> countByStatus(String status);

    Mono<Long> countByCustomerIdAndStatus(String customerId, String status);

    Flux<Account> findAllBy(Pageable pageable);

    Flux<Account> findAllByCustomerId(String customerId, Pageable pageable);

    Flux<Account> findAllByStatus(String status, Pageable pageable);

    Flux<Account> findAllByCustomerIdAndStatus(String customerId, String status, Pageable pageable);
}
