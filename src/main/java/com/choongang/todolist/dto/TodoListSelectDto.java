package com.choongang.todolist.dto;

import com.choongang.todolist.domain.TodoStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TodoListSelectDto {
	
    private Long todoId;
    private String title;
    private TodoStatus status;     // TODO / DOING / DONE
    private LocalDateTime dueAt;
    private String ddayLabel;   // 마감 D-day 표시용 (예: D-3, D-Day, D+2)
	
}
