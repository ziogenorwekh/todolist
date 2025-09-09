package com.choongang.todolist.domain;

import com.choongang.todolist.dto.UserCreateRequestDto;
import lombok.*;

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

    @Builder
    public User(Long userId, String username, String password, String email,
                LocalDateTime createAt, LocalDateTime updateAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }


    // 도메인에서 직접 생성하는 메서드를 작성할 수 있습니다.
//    public static User createUser(UserCreateRequestDto userCreateRequestDto) {
//        return User.builder()
//                .username(userCreateRequestDto.getName())
//                .password(userCreateRequestDto.getPassword())
//                .email(userCreateRequestDto.getEmail())
//                .createAt(LocalDateTime.now())
//                .updateAt(null)
//                .build();
//    }

}
