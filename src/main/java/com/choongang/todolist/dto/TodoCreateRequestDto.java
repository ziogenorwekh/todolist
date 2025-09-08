package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.domain.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCreateRequestDto {

    @NotBlank
    private String title;
    private String content;
    @NotNull
    private Priority priority;
    @NotNull
    private TodoStatus status;
    @NotNull
    private LocalDateTime dueAt;

}
