package com.thaovo.shoppingcart.admin.service;

import com.thaovo.shoppingcart.admin.model.dto.UserReqDto;
import com.thaovo.shoppingcart.admin.model.dto.UserResponseDto;
import com.thaovo.shoppingcart.user.authentication.exceptions.DataValidationException;

import java.util.List;

public interface AdminService {
    UserResponseDto insert(UserReqDto user) throws DataValidationException;

    UserResponseDto modify(UserReqDto user) throws DataValidationException;

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByUsername(String username);

    UserResponseDto destroy(String username);
}
