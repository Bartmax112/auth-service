package com.wspa.auth.exceptions;

import java.text.MessageFormat;
import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(UUID userId) {
        super(MessageFormat.format("User was not found by given UUID = {0}", userId));
    }
}
