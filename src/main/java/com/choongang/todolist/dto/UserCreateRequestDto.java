package com.choongang.todolist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateRequestDto {

    // 해당 주석들은 @Valid에서 사용하는 일종의 제약과 같습니다.
    // @Email은 해당 문자열이 이메일 형식인지 확인해요.
    // @NotBlank는, 해당 문자열이 비었는지? @NotNull은 해당 문자열이 Null값인지 판단합니다.
    // @NotNull은 단순히 문자열이 null 값인지만 판단해요.
    // 이 제약조건의 주석은 사용하려는 컨트롤러에서 @Valid를 선언하면 자동으로 검사합니다.
//    @Email(message = "이메일만 입력 할 수 있습니다.")
    private String email;
//    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
//    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String confirmPassword;
//    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    // 기본 생성자
    public UserCreateRequestDto() {}

    // 전체 생성자
    public UserCreateRequestDto(String email, String password, String confirmPassword, String name) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
    }

    // getter/setter 메서드들
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
