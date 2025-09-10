package com.choongang.todolist.service;

import com.choongang.todolist.dao.TodoDao;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoUpdateRequestDto;
import com.choongang.todolist.exception.TodoOwnershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;


    @Override
    public Todo findById(Long id) {
    	return todoDao.findTodoById(id);
    }

    @Override
    public Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = Todo.createTodo(todoCreateRequestDto, userId);
        // todoDao에서 생성된 Todo가 있다면 리턴해주세요.
        return todoDao.saveTodo(todo);
    }

    @Override
    public Todo updateTodo(TodoUpdateRequestDto todoUpdateRequestDto, Long todoId, Long userId) {
        Todo todo = todoDao.findTodoById(todoId);
        if (!todo.getUserId().equals(userId)) {
            throw new TodoOwnershipException("Todo owner is unauthorized");
        }
        todo.setTitle(todoUpdateRequestDto.getTitle());
        todo.setContent(todoUpdateRequestDto.getContent());
        todo.setPriority(todoUpdateRequestDto.getPriority());
        todo.setStatus(todoUpdateRequestDto.getStatus());
        todo.setDueAt(todoUpdateRequestDto.getDueAt());
        return todoDao.updateTodo(todo);
    }
}
