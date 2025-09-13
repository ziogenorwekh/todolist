package com.choongang.todolist.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.choongang.todolist.dto.UserUpdateDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  ===== UserUpdateDto Test =====
 *  입력값 검증(Validation)
 */
public class UserUpdateDtoTest {
	private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("정상 입력값일 때 검증 에러가 없어야 한다")
    void validInput_ok() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setDisplayName("Haeun");
        dto.setEmail("test@example.com");
        dto.setProfileImageUrl("http://image.com/profile.png");
        dto.setNewPassword("pw1234");
        dto.setNewPasswordConfirm("pw1234");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "정상 값이면 검증 에러가 없어야 함");
    }

    @Test
    @DisplayName("displayName이 비어있으면 @NotBlank 검증 오류 발생")
    void displayName_blank_error() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setDisplayName("   "); // 공백만
        dto.setEmail("test@example.com");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("displayName")
                        && v.getMessage().contains("표시 이름")
        ));
    }

    @Test
    @DisplayName("이메일 형식이 잘못되면 @Email 검증 오류 발생")
    void email_invalid_error() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setDisplayName("Haeun");
        dto.setEmail("not-an-email"); // 잘못된 형식

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("email")
                        && v.getMessage().contains("이메일 형식")
        ));
    }

    @Test
    @DisplayName("비밀번호 확인 필드가 달라도 Validator 제약은 걸리지 않는다")
    void newPasswordConfirm_notValidatedYet() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setDisplayName("Haeun");
        dto.setEmail("test@example.com");
        dto.setNewPassword("pw1");
        dto.setNewPasswordConfirm("pw2"); // 다름

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);

        // 현재 @NotBlank, @Email만 있어서 비번 불일치 자체는 Validator에서 체크하지 않음
        assertTrue(violations.isEmpty(), "비밀번호 불일치는 별도 커스텀 검증 로직 필요");
    }
}
