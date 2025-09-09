package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);

    void saveUser(User user);
    Optional<User> findUserById(Long id);
    void updateUser(User user);
    void deleteUserById(Long id);
    void deleteUserByEmail(String email);
}