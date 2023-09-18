package ru.skillbox.socialnetwork.messages.exception.exceptions;

public class EmailIsBlankException extends RuntimeException{
    public EmailIsBlankException(String message) {
        super(message);
    }
}
