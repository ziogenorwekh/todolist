package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Priority priority;
    @NotNull
    private TodoStatus status;
    @NotNull
    private LocalDateTime dueAt;
    private LocalDateTime completedAt;
}
