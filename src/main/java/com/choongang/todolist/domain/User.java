package com.choongang.todolist.domain;

import com.choongang.todolist.dto.UserCreateRequestDto;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
//@Builder // 빌더 패턴을 사용하여 객체 생성
public class User {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String profileImageUrl;    // JHE 추가
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
    /**
     * @param password 변경할 비밀번호
     * @param email 변경할 이메일
     */
    public void update(String password, String email) {
        this.password = password;
        this.email = email;
        this.updateAt = LocalDateTime.now();
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
