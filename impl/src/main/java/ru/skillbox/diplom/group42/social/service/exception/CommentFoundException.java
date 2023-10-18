package ru.skillbox.diplom.group42.social.service.exception;

public class CommentFoundException  extends  RuntimeException{
    @Override
    public String getMessage() {
        return "Comment not found";
    }
}
