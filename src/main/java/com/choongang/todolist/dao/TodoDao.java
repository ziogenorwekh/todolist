package com.choongang.todolist.dao;

import java.util.List;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoSearchCond;


/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */

public interface TodoDao {

    Todo saveTodo (Todo todo);
    Todo findTodoById(Long id); 
    List<Todo> selectTodosByUser(Long userId, TodoSearchCond cond);  // 로그인 사용자(userId) 소유의 Todo 목록 조회 JHE
    long countTodosByUser(Long userId, TodoSearchCond cond);   // 사용자(userId) 소유의 조회 결과의 전체 개수
    Todo updateTodo (Todo todo);
    int deleteTodo(Long todoId);
}

