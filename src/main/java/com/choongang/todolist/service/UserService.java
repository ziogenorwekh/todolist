package com.choongang.todolist.service;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 사용자 ID(username)와 비밀번호로 로그인을 처리합니다.
     *
     * @param username 로그인할 사용자 ID
     * @param password 입력된 비밀번호
     * @return 로그인이 성공하면 User 객체를, 실패하면 Optional.empty()를 반환합니다.
     */
    public Optional<User> login(String username, String password) {
        // 1. username으로 사용자를 조회합니다.
        Optional<User> userOptional = userDao.findByUsername(username);

        // 2. 사용자가 존재하고 비밀번호가 일치하는지 확인합니다.
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 실제 구현에서는 암호화된 비밀번호를 비교해야 합니다.
            if (user.getPassword().equals(password)) {
                return userOptional; // 로그인 성공
            }
        }

        // 3. 사용자 정보가 없거나 비밀번호가 일치하지 않으면 로그인 실패
        return Optional.empty();
    }
}