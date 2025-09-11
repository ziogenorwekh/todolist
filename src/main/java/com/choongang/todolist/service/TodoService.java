package com.choongang.todolist.service;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.PageResponse;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoUpdateRequestDto;

import com.choongang.todolist.dto.TodoListSelectDto;
import com.choongang.todolist.dto.TodoSearchCond;

import java.util.List;

/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */
public interface TodoService {
	Todo findById(Long id);
    Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId);

    Todo updateTodo(TodoUpdateRequestDto todoUpdateRequestDto, Long todoId, Long userId);
    List<TodoListSelectDto> retrieveTodos(Long userId);  // 조건 없이(기본값) 로그인 사용자의 투두 목록 JHE
	PageResponse<TodoListSelectDto> retrieveTodos(Long userId, TodoSearchCond cond); // 조건/정렬/페이징/D-day까지 포함한 목록

}

