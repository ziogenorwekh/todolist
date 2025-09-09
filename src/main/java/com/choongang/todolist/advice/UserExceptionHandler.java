package com.choongang.todolist.advice;

import com.choongang.todolist.exception.UserNotFoundException;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmailException(DuplicateEmailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "auth/register";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public String handleInvalidPasswordException(InvalidPasswordException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "auth/delete-account";
    }
}