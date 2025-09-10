package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateRequestDto {

    private String title;
    private String content;
    private Priority priority;
    private TodoStatus status;
    private LocalDateTime dueAt;
    private LocalDateTime completedAt;
}
