package ru.skillbox.diplom.group42.social.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCaptchaException extends ResponseStatusException {

    public InvalidCaptchaException() {
        super(HttpStatus.BAD_REQUEST, "In AuthService register: captcha failed");
    }
}
