package com.choongang.todolist.exception;

public class InvalidPasswordException extends TodoException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}