package dev.magadiflo.accounts.app.mapper;

import dev.magadiflo.accounts.app.domain.entity.Account;

import dev.magadiflo.accounts.app.model.AccountPageResponse;
import dev.magadiflo.accounts.app.model.AccountRequest;
import dev.magadiflo.accounts.app.model.AccountResponse;
import dev.magadiflo.accounts.app.model.AccountStatus;
import dev.magadiflo.accounts.app.model.AccountUpdateRequest;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {BigDecimal.class, AccountStatus.class}
)
public interface AccountMapper {
    AccountResponse toAccountResponse(Account account);

    @Mapping(source = "number", target = "pageNumber")
    @Mapping(source = "size", target = "pageSize")
    AccountPageResponse toAccountPageResponse(Page<AccountResponse> page);

    @Mapping(target = "balance", expression = "java(request.getInitialDeposit() != null ? request.getInitialDeposit() : BigDecimal.ZERO)")
    @Mapping(target = "status", expression = "java(AccountStatus.ACTIVE.getValue())")
    Account toAccount(AccountRequest request);

    /**
     * ignoreByDefault = true:
     * Ignora todos los mapeos de forma predeterminada.
     * Todos los campos que se deseen mapear deben declararse explícitamente.
     * No se realizará ningún mapeo automático.
     * Tampoco se generarán advertencias por propiedades de origen o destino no mapeadas.
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "alias", target = "alias")
    Account toUpdateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}
