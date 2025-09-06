package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;

public interface UserDao {

    User findUserByEmail(String email);

    void saveUser(User user);
}
