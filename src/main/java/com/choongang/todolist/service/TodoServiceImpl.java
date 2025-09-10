package com.choongang.todolist.service;

import com.choongang.todolist.dao.TodoDao;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.PageResponse;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoListSelectDto;
import com.choongang.todolist.dto.TodoSearchCond;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;

    @Autowired
    public TodoServiceImpl(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = Todo.createTodo(todoCreateRequestDto, userId);
        // todoDao에서 생성된 Todo가 있다면 리턴해주세요.
        Todo saved = todoDao.saveTodo(todo);
        return saved;
    }
    
    
    /** 컨트롤러에서 간단 조회용으로 호출 (cond 기본값 사용) */
    @Override
    @Transactional(readOnly = true)
    public List<TodoListSelectDto> retrieveTodos(Long userId) {
        TodoSearchCond cond = new TodoSearchCond(); // page=1,size=10,sort=createdAt,dir=desc …
        PageResponse<TodoListSelectDto> page = retrieveTodos(userId, cond);
        return page.getContent();
    }
    
    /** 필터/정렬/페이징 전체 처리 */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<TodoListSelectDto> retrieveTodos(Long userId, TodoSearchCond cond) {
        List<Todo> rows  = todoDao.selectTodosByUser(userId, cond);
        long total       = todoDao.countTodosByUser(userId, cond);

        List<TodoListSelectDto> items = rows.stream()
            .map(t -> TodoListSelectDto.builder()
                .todoId(t.getTodoId())          // ← 엔티티 필드명에 맞춤
                .title(t.getTitle())
                .status(t.getStatus())
                .dueAt(t.getDueAt())
                .ddayLabel(computeDdayLabel(t.getDueAt()))
                .build()
            )
            .toList();

        return new PageResponse<TodoListSelectDto>(items, cond.getPage(), cond.getSize(), total);
    }
    
    /** D-day 계산 */
    private String computeDdayLabel(LocalDateTime dueAt) {
        if (dueAt == null) return "-";
        LocalDate today = LocalDate.now();
        LocalDate due = dueAt.toLocalDate();
        long diff = java.time.temporal.ChronoUnit.DAYS.between(today, due);
        if (diff == 0) return "D-Day";
        return diff > 0 ? "D-" + diff : "D+" + Math.abs(diff);
    }

	
}
