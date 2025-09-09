package com.choongang.todolist.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
@Builder // 빌더 패턴을 사용하여 객체 생성
public class User {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    /**
     * @param password 변경할 비밀번호
     * @param email 변경할 이메일
     */
    public void update(String password, String email) {
        this.password = password;
        this.email = email;
        this.updateAt = LocalDateTime.now();
    }
}