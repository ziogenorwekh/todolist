package com.choongang.todolist.domain;

import com.choongang.todolist.dto.TodoCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Todo {

    private Long todoId;
    private Long userId;
    private String title;
    private String content;
    private Priority priority;
    private TodoStatus status;
    private LocalDateTime dueAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;


    public static Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = new Todo(null,userId, todoCreateRequestDto.getTitle(),todoCreateRequestDto.getContent(),
                todoCreateRequestDto.getPriority(), todoCreateRequestDto.getStatus(), todoCreateRequestDto.getDueAt(),
                LocalDateTime.now(), null, null);
        return todo;

    }
}
