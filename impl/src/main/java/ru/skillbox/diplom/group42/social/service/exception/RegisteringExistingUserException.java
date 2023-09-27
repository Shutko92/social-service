package ru.skillbox.diplom.group42.social.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegisteringExistingUserException extends ResponseStatusException {

    public RegisteringExistingUserException() {
        super(HttpStatus.BAD_REQUEST, "User already exists");
    }
}
