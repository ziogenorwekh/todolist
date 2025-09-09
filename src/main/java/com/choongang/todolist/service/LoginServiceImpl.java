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

    /**
     * 로그인을 처리합니다.
     * 1. 이메일로 유저를 찾습니다.
     * 2. 유저가 존재하지 않으면, UserLoginException 예외를 발생시킵니다.
     * 3. 유저가 존재한다면, 비밀번호가 일치하는지 확인합니다.
     * 4. 비밀번호가 일치하지 않으면, UserLoginException 예외를 발생시킵니다.
     * 5. 비밀번호가 일치한다면, 유저 객체를 반환합니다.
     *
     * @param email 로그인 하기 위한 유저의 이메일 값
     * @param password 로그인 하기 위한 유저의 비밀번호 값
     * @return 로그인이 성공하면 유저 객체를 반환
     * @throws UserLoginException 로그인이 실패하면 예외를 발생
     */
    @Override
    public User login(String email, String password) {
        // 1. 이메일로 유저 찾기 (메서드명 통일)
        User user = userDao.findByEmail(email); // findUserByEmail -> findByEmail로 통일

        // 2. 유저가 존재하지 않으면 예외 발생
        if (user == null) {
            throw new UserLoginException(String.format("User not found for email: %s", email));
        }

        // 3. 비밀번호 일치하지 않으면 예외 발생 (논리 오류 수정)
        if (!user.getPassword().equals(password)) {
            throw new UserLoginException(String.format("Invalid password for email: %s", email));
        }

        // 4. 로그인 성공 시 유저 반환
        return user;
    }
}