package com.choongang.todolist.todo;

import com.choongang.todolist.domain.TodoStatus;
import com.choongang.todolist.dto.TodoListSelectDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  ===== TodoListSelectDto Test =====
 * 빌더 / 생성자로 값이 잘 세팅되는지
 * toString() 이 @ToString 롬복으로 잘 동작하는지
 * 
 */
public class TodoListSelectDtoTest {
	
	@Test
    @DisplayName("빌더로 생성 시 모든 필드가 올바르게 세팅된다")
    void createWithBuilder_ok() {
        LocalDateTime dueAt = LocalDateTime.of(2025, 9, 13, 12, 0);

        TodoListSelectDto dto = TodoListSelectDto.builder()
                .id(1L)
                .title("테스트 제목")
                .status(TodoStatus.TODO)
                .dueAt(dueAt)
                .ddayLabel("D-3")
                .build();

        assertAll(
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("테스트 제목", dto.getTitle()),
                () -> assertEquals(TodoStatus.TODO, dto.getStatus()),
                () -> assertEquals(dueAt, dto.getDueAt()),
                () -> assertEquals("D-3", dto.getDdayLabel())
        );
    }

    @Test
    @DisplayName("전체 생성자를 사용해도 필드가 올바르게 세팅된다")
    void createWithAllArgsConstructor_ok() {
        LocalDateTime dueAt = LocalDateTime.of(2025, 9, 13, 18, 0);

        TodoListSelectDto dto = new TodoListSelectDto(
                2L,
                "두 번째 할 일",
                TodoStatus.DONE,
                dueAt,
                "D-Day"
        );

        assertAll(
                () -> assertEquals(2L, dto.getId()),
                () -> assertEquals("두 번째 할 일", dto.getTitle()),
                () -> assertEquals(TodoStatus.DONE, dto.getStatus()),
                () -> assertEquals(dueAt, dto.getDueAt()),
                () -> assertEquals("D-Day", dto.getDdayLabel())
        );
    }

    @Test
    @DisplayName("toString()이 필드 값을 포함한다")
    void toString_containsFields() {
        TodoListSelectDto dto = TodoListSelectDto.builder()
                .id(3L)
                .title("테스트 toString")
                .status(TodoStatus.DOING)
                .ddayLabel("D+2")
                .build();

        String str = dto.toString();

        assertTrue(str.contains("테스트 toString"));
        assertTrue(str.contains("DOING"));
        assertTrue(str.contains("D+2"));
    }
}
