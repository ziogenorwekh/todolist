package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Todo;


/***
 * 메서드를 수정해야 한다면, 수정해도 좋습니다. 해당 인터페이스들의 메서드는 예시입니다.
 */

public interface TodoDao {

    Todo saveTodo (Todo todo);
    Todo findTodoById(Long id);
}

