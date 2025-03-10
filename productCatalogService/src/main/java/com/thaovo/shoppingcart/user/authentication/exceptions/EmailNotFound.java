package com.thaovo.shoppingcart.user.authentication.exceptions;

public class EmailNotFound extends Exception {

    public EmailNotFound() {
        super();
    }

    public EmailNotFound(String message) {
        super(message);
    }

    public EmailNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotFound(Throwable cause) {
        super(cause);
    }
}
