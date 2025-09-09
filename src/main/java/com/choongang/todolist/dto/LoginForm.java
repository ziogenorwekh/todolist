package com.choongang.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * @ModelAttribute로, 필드에 필요한 값을 자동 지정하여 매핑합니다.
 * 로그인 폼 DTO
 * email과 password 필드를 포함
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    private String email;
    private String password;

    public String getUsername() {
        return email;
    }
}
