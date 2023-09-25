package ru.skillbox.diplom.group42.social.service.exception;

public class PostFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Post not found";
    }
}
