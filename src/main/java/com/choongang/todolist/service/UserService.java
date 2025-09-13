package com.choongang.todolist.service;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.dto.UserUpdateDto;

public interface UserService {

    /**
     * 새로운 사용자 생성
     */
    User createUser(UserCreateRequestDto userCreateRequestDto);

    /**
     * 사용자 삭제
     */
    void deleteUser(Long userId, String password);
    
    /**
     * 사용자 수정
     */
    void updateUser(Long targetUserId, Long actorUserId, UserUpdateDto Dto);
}
