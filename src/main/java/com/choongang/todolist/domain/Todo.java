package com.choongang.todolist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public Todo(Long todoId, Long userId, String title, String content, Priority priority, TodoStatus status,
                LocalDateTime dueAt, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime completedAt) {
        this.todoId = todoId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.status = status;
        this.dueAt = dueAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }


    /**
     * 투두 객체에서 수정이나 값 주입에 필요한 메서드들을 아래에 기입해주세요.
     */
}
