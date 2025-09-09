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
        // 이메일 중복 확인
        User existingUser = userDao.findByEmail(userCreateRequestDto.getEmail());
        if (existingUser != null) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다: " + userCreateRequestDto.getEmail());
        }

        // 주석을 참고해주세요.
        // 유저 객체를 Builder를 사용해서 생성하시거나, 생성자를 이용해서 사용하는 것이 조금 더 바람직 합니다.
//        User user = User.createUser(userCreateRequestDto);
//        유저의 updateAt은 새롭게 생성한 유저라면, 업데이트 기록이 없으니 null 처리 해주시면 좋을 것 같습니다.

        // User 객체 생성
        User user = new User();
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPassword(userCreateRequestDto.getPassword());
        user.setUsername(userCreateRequestDto.getName());
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        // 데이터베이스에 저장
        userDao.insertUser(user);

        return user;
    }

    @Override
    public void deleteUser(Long userId, String password) {
        // 사용자 존재 확인
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
        }

        // 사용자 삭제
        userDao.deleteUser(userId);
    }
}
