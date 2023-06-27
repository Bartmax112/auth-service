package com.wspa.auth.exceptions;

import java.text.MessageFormat;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String username) {
        super(MessageFormat.format("Wrong login credentials for username: {0}", username));
    }
}
