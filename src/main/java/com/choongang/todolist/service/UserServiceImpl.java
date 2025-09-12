package com.choongang.todolist.service;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import com.choongang.todolist.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        // 이메일 중복 검사
        if (userDao.findByEmail(userCreateRequestDto.getEmail()) != null) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다: " + userCreateRequestDto.getEmail());
        }

        User user = User.builder()
                .email(userCreateRequestDto.getEmail())
                .password(userCreateRequestDto.getPassword())
                .username(userCreateRequestDto.getName())
                .createAt(LocalDateTime.now())
                .updateAt(null)
                .build();

        int rows = userDao.insertUser(user);
        if (rows != 1) {
            throw new RuntimeException("사용자 생성에 실패했습니다.");
        }

        return user;
    }

    @Override
    public void deleteUser(Long userId, String password) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }

        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
        }

        int rows = userDao.deleteUser(userId);
        if (rows != 1) {
            throw new RuntimeException("사용자 삭제에 실패했습니다.");
        }
    }
}