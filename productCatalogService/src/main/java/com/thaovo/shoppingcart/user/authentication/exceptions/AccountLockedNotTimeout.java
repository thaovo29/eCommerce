package com.thaovo.shoppingcart.user.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccountLockedNotTimeout extends AuthenticationException {
    public AccountLockedNotTimeout(String msg) {
        super(msg);
    }
}
