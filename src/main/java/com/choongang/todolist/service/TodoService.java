package com.choongang.todolist.service;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoCreateRequestDto;

public interface TodoService {

    Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId);
}
