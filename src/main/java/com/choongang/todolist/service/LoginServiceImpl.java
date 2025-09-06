package com.choongang.todolist.service;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.exception.UserLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserDao userDao;

    @Autowired
    public LoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /***
     * 로그인을 처리할거에요.
     * 1. 이메일로 유저를 찾을거에요.
     * 2. 유저가 존재하지 않으면, UserLoginException 예외를 발생시킬거에요.
     * 3. 유저가 존재한다면, 비밀번호가 일치하는지 확인할거에요.
     * 4. 비밀번호가 일치하지 않으면, UserLoginException 예외를 발생시킬거에요.
     * 5. 비밀번호가 일치한다면, 유저 객체를 반환할거에요.
     * @param email 로그인 하기 위한 유저의 이메일 값이에요.
     * @param password 로그인 하기 위한 유저의 비밀번호 값이에요.
     * @return 로그인이 성공한다면, 유저 객체를 반환할거에요.
     * @throws UserLoginException 로그인이 실패한다면, UserLoginException 예외를 발생시킬거에요.
     */
    @Override
    public User login(String email, String password) {
        User user = userDao.findUserByEmail(email);
        if (user == null && !user.getPassword().equals(password)) {
            throw new UserLoginException(String.format("User login failed for email: %s", email));
        }
        return user;
    }
}
