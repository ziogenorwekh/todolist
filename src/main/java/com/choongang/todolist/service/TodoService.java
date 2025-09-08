package com.choongang.todolist.service;

import com.choongang.todolist.domain.Todo;

import java.util.List;

/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */
public interface TodoService {

    Todo createTodo(/*투두 생성에 필요한 파라미터를 입력해주세요.*/);

    Todo updateTodo(/*투두 내용 수정에 필요한 파라미터를 입력해주세요.*/);

    Todo findTodoById(Long todoId);

    List<Todo> findAllTodos(Long userId);

    void deleteTodoById(Long todoId);
}
