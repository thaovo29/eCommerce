package com.thaovo.shoppingcart.admin.model.exception;

public class UserUsernameNotFoundException extends RuntimeException{
    public UserUsernameNotFoundException(String message) {
        super(message);
    }
}
