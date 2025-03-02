package com.thaovo.shoppingcart.user.address.model.service;

import com.thaovo.shoppingcart.user.address.dto.AddressDto;
import com.thaovo.shoppingcart.user.authentication.exceptions.DataValidationException;

import java.util.List;

public interface AddressService {

    void addAddress(AddressDto addressDto, String username) throws DataValidationException;

    List<AddressDto> getAllAddress(String username);

    AddressDto updateAddress(AddressDto addressDto, String username) throws DataValidationException;

    boolean deleteAddress(AddressDto addressDto, String username) throws DataValidationException;

    AddressDto getDefaultAddress(String username);
}
