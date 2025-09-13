package com.choongang.todolist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter
public class UserUpdateDto {
    // 프로필(표시 정보)
    @NotBlank(message = "표시 이름을 입력하세요.")
    private String displayName;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    // 프로필 이미지 URL(선택)
    private String profileImageUrl;

    // 비밀번호 변경(선택)
    private String newPassword;
    private String newPasswordConfirm;
}