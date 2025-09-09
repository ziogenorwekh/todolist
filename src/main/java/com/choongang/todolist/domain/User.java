package com.choongang.todolist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public User(Long userId, String username, String password, String email,
                LocalDateTime createAt, LocalDateTime updateAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    /**
     * 유저 객체에서 수정이나 값 주입에 필요한 메서드들을 아래에 기입해주세요.
     */

}
