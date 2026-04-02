package com.banking.smartbank.mapper;

import com.banking.smartbank.domain.Transfer;
import com.banking.smartbank.dto.response.TransferResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferResponse toResponse(Transfer transfer);
}
