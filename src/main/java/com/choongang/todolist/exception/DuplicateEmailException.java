package com.choongang.todolist.exception;

import com.choongang.todolist.domain.Todo;

public class DuplicateEmailException extends TodoException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}