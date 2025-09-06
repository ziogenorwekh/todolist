package com.choongang.todolist.exception;

/***
 * 사용자 조회 실패 시 발생하는 예외 클래스
 */
public class UserNotFoundException extends TodoException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
