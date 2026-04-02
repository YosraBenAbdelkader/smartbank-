package com.banking.smartbank.mapper;

import com.banking.smartbank.domain.Account;
import com.banking.smartbank.dto.response.AccountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
}