package com.choongang.todolist.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.choongang.todolist.domain.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** 
 * ===== User Domain Test =====
 * 생성자/빌더로 객체가 정상적으로 만들어지는지 확인
 * update 메서드 호출 시 비밀번호, 이메일, updateAt 값이 잘 바뀌는지 확인
 */

public class UserTest {
	@Test
    @DisplayName("빌더로 생성 시 필드가 올바르게 세팅된다")
    void createUser_withBuilder_ok() {
        LocalDateTime created = LocalDateTime.now();

        User user = User.builder()
                .userId(1L)
                .username("haeun")
                .password("1234")
                .email("test@example.com")
                .createAt(created)
                .build();

        assertAll(
                () -> assertEquals(1L, user.getUserId()),
                () -> assertEquals("haeun", user.getUsername()),
                () -> assertEquals("1234", user.getPassword()),
                () -> assertEquals("test@example.com", user.getEmail()),
                () -> assertEquals(created, user.getCreateAt()),
                () -> assertNull(user.getUpdateAt(), "처음엔 updateAt이 null")
        );
    }

    @Test
    @DisplayName("update 호출 시 비밀번호/이메일/수정일시가 변경된다")
    void updateUser_success() {
        User user = User.builder()
                .userId(1L)
                .username("haeun")
                .password("oldPw")
                .email("old@example.com")
                .createAt(LocalDateTime.now())
                .build();

        // when
        user.update("newPw", "new@example.com");

        // then
        assertAll(
                () -> assertEquals("newPw", user.getPassword()),
                () -> assertEquals("new@example.com", user.getEmail()),
                () -> assertNotNull(user.getUpdateAt(), "updateAt은 갱신되어야 함"),
                () -> assertTrue(user.getUpdateAt().isBefore(LocalDateTime.now().plusSeconds(1)))
        );
    }

    @Test
    @DisplayName("update를 여러번 호출하면 updateAt도 갱신된다")
    void updateUser_multipleTimes_updateAtChanges() throws InterruptedException {
        User user = User.builder()
                .userId(1L)
                .username("haeun")
                .password("pw1")
                .email("a@example.com")
                .createAt(LocalDateTime.now())
                .build();

        user.update("pw2", "b@example.com");
        LocalDateTime firstUpdateAt = user.getUpdateAt();

        Thread.sleep(10); // updateAt 값 차이를 주기 위해 잠시 대기
        user.update("pw3", "c@example.com");

        assertTrue(user.getUpdateAt().isAfter(firstUpdateAt),
                "두 번째 updateAt은 첫 번째보다 나중이어야 함");
    }
}
