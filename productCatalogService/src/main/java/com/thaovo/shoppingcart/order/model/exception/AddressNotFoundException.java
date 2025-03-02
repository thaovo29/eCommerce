package com.thaovo.shoppingcart.order.model.exception;

public class AddressNotFoundException extends Exception{
    public AddressNotFoundException(String message) {
        super(message);
    }
}