package com.wspa.auth.exceptions;

import java.text.MessageFormat;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException(String token) {
        super(MessageFormat.format("User by given UUID: {0} is not logged in", token));
    }
}
