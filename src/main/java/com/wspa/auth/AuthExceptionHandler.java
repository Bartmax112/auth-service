package com.wspa.auth;

import com.wspa.auth.exceptions.AuthorizationException;
import com.wspa.auth.exceptions.UserNotLoggedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    ResponseEntity<String> handle(AuthorizationException exception) {
        log.error("AuthorizationException handling");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(UserNotLoggedException.class)
    ResponseEntity<String> handle(UserNotLoggedException exception) {
        log.error("UserNotLoggedException handling");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }


}
