package com.choongang.todolist.service;

import com.choongang.todolist.domain.User;

public interface LoginService {
    // static 제거하고 인스턴스 메서드로 변경
    User login(String email, String password);
}