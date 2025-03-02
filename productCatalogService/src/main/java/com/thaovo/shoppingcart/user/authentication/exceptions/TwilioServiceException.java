package com.thaovo.shoppingcart.user.authentication.exceptions;

public class TwilioServiceException extends Exception {

    public TwilioServiceException() {
        super();
    }

    public TwilioServiceException(String message) {
        super(message);
    }

    public TwilioServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwilioServiceException(Throwable cause) {
        super(cause);
    }
}
