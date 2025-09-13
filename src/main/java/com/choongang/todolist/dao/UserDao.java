package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;
import java.util.Optional;

public interface UserDao {

    int insertUser(User user);
    User findByEmail(String email);
    User findById(Long id);
    int updateProfile(Long userId, String displayName, String email, String profileImageUrl); // JHE
    int updatePassword(Long userId, String passwordHash);   // JHE
    int deleteUser(Long userId);
    int saveUser(User user);
}