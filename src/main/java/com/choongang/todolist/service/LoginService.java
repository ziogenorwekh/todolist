package com.choongang.todolist.service;

import com.choongang.todolist.domain.User;

public interface LoginService {

    User login(String email, String password);

}
