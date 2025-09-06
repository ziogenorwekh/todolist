package com.choongang.todolist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
