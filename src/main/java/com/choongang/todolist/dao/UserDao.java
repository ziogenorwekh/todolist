package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;

/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */
public interface UserDao {

    User findUserByEmail(String email);

    /**
     * 필요하면, 유저 객체를 리턴하는 방식으로 바꾸어도 됩니다.
     * @param user
     */
    void saveUser(User user);

    User findUserById(Long id);

    /**
     * 필요하면, 유저 객체를 리턴하는 방식으로 바꾸어도 됩니다.
     * @param user
     */
    void updateUser(User user);

    void deleteUserById(Long id);

    void deleteUserByEmail(String email);
}
