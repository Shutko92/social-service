package ru.skillbox.diplom.group42.social.service.exception.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skillbox.diplom.group42.social.service.exception.CommentFoundException;
import ru.skillbox.diplom.group42.social.service.exception.PostFoundException;
import ru.skillbox.diplom.group42.social.service.exception.ResourceFoundException;
@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({PostFoundException.class, CommentFoundException.class, ResourceFoundException.class})
    public String customNotFound(Exception e){
        log.debug(e.getMessage());
        return e.getMessage();
    }
}

