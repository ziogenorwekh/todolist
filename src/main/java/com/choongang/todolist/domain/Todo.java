package com.choongang.todolist.domain;


import com.choongang.todolist.dto.TodoCreateRequestDto;

import com.choongang.todolist.dto.TodoUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



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

    public static Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = new Todo(null,userId, todoCreateRequestDto.getTitle(),todoCreateRequestDto.getContent(),
                todoCreateRequestDto.getPriority(), todoCreateRequestDto.getStatus(), todoCreateRequestDto.getDueAt(),
                LocalDateTime.now(), null, null);
        return todo;
    }

    public void done() {
        this.status = TodoStatus.DONE;
        this.updatedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public void doing() {
        this.status = TodoStatus.DOING;
        this.updatedAt = LocalDateTime.now();
    }


}
