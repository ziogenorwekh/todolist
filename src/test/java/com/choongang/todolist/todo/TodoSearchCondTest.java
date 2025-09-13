package com.choongang.todolist.todo;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;
import com.choongang.todolist.dto.TodoSearchCond;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  ====== TodoSearchCond Test ======
 * @AllArgsConstructor와 @NoArgsConstructor 동작 확인
 * sort, dir, page, size 기본값이 잘 들어가는지
 * getOffset() 계산이 의도대로 되는지
 * 
 */
public class TodoSearchCondTest {
	@Test
    @DisplayName("기본 생성 시 기본값이 세팅된다")
    void defaultConstructor_hasDefaultValues() {
        TodoSearchCond cond = new TodoSearchCond();

        assertAll(
                () -> assertEquals("createdAt", cond.getSort()),
                () -> assertEquals("desc", cond.getDir()),
                () -> assertEquals(1, cond.getPage()),
                () -> assertEquals(10, cond.getSize())
        );
    }

    @Test
    @DisplayName("모든 필드를 받는 생성자로 값이 세팅된다")
    void allArgsConstructor_setsValues() {
        LocalDate from = LocalDate.of(2025, 9, 1);
        LocalDate to = LocalDate.of(2025, 9, 30);

        TodoSearchCond cond = new TodoSearchCond(
                TodoStatus.TODO,
                Priority.HIGH,
                "공부",
                from,
                to,
                "title",
                "asc",
                2,
                20
        );

        assertAll(
                () -> assertEquals(TodoStatus.TODO, cond.getStatus()),
                () -> assertEquals(Priority.HIGH, cond.getPriority()),
                () -> assertEquals("공부", cond.getKeyword()),
                () -> assertEquals(from, cond.getDueFrom()),
                () -> assertEquals(to, cond.getDueTo()),
                () -> assertEquals("title", cond.getSort()),
                () -> assertEquals("asc", cond.getDir()),
                () -> assertEquals(2, cond.getPage()),
                () -> assertEquals(20, cond.getSize())
        );
    }

    @Test
    @DisplayName("getOffset()은 page * size 를 반환한다")
    void getOffset_returnsPageTimesSize() {
        TodoSearchCond cond = new TodoSearchCond();
        cond.setPage(3);
        cond.setSize(15);

        int offset = cond.getOffset();

        assertEquals(45, offset);
    }
}
