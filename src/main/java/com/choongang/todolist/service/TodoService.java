package com.choongang.todolist.service;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoUpdateRequestDto;


/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */
public interface TodoService {
	Todo findById(Long id);
    Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId);

    Todo updateTodo(TodoUpdateRequestDto todoUpdateRequestDto, Long todoId, Long userId);
}

