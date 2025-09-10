package com.choongang.todolist.service;

import com.choongang.todolist.dao.TodoDao;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;

    @Override
    public Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = Todo.createTodo(todoCreateRequestDto, userId);
        // todoDao에서 생성된 Todo가 있다면 리턴해주세요.
        return todoDao.saveTodo(todo);
    }

    @Override
    public Todo updateTodo(TodoUpdateRequestDto todoUpdateRequestDto, Long todoId) {
        Todo todo = todoDao.findTodoById(todoId);
        if (todoUpdateRequestDto.getTitle() != null) {
            todo.setTitle(todoUpdateRequestDto.getTitle());
        }
        if (todoUpdateRequestDto.getContent() != null) {
            todo.setContent(todoUpdateRequestDto.getContent());
        }
        if (todoUpdateRequestDto.getPriority() != null) {
            todo.setPriority(todoUpdateRequestDto.getPriority());
        }
        if (todoUpdateRequestDto.getStatus() != null) {
            todo.setStatus(todoUpdateRequestDto.getStatus());
        }
        if (todoUpdateRequestDto.getDueAt() != null) {
            todo.setDueAt(todoUpdateRequestDto.getDueAt());
        }
        if (todoUpdateRequestDto.getCompletedAt() != null) {
            todo.setCompletedAt(todoUpdateRequestDto.getCompletedAt());
        }
        return todoDao.updateTodo(todo);
    }
}
