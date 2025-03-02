package com.thaovo.shoppingcart.admin.model.exception;

public class UserUsernameAlreadyExist extends RuntimeException{
    public UserUsernameAlreadyExist(String message) {
        super(message);
    }
}
