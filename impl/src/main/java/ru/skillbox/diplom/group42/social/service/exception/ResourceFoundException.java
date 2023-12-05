package ru.skillbox.diplom.group42.social.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceFoundException extends ResponseStatusException {
    
    public ResourceFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

}
