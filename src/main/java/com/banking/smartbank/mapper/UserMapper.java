package com.banking.smartbank.mapper;

import com.banking.smartbank.domain.User;
import com.banking.smartbank.dto.response.UserResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
