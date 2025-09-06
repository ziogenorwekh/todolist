package com.choongang.todolist.domain;

import java.time.LocalDateTime;

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
}
