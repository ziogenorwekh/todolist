package com.choongang.todolist.service;

import com.choongang.todolist.dao.TodoDao;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;

    @Autowired
    public TodoServiceImpl(TodoDao todoDao) {
        this.todoDao = todoDao;
    }
    @Override
    public Todo findById(Long id) {
    	return todoDao.findTodoById(id);
    }
    @Override
    public Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = Todo.createTodo(todoCreateRequestDto, userId);
        // todoDao에서 생성된 Todo가 있다면 리턴해주세요.
        Todo saved = todoDao.saveTodo(todo);
        return saved;
    }
}
