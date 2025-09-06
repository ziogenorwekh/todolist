package com.choongang.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * 로그인 폼 DTO
 * email과 password 필드를 포함
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    private String email;
    private String password;
}
