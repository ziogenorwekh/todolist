package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Todo;

public interface TodoDao {

    Todo saveTodo (Todo todo);
    Todo findTodoById(String id);
}

