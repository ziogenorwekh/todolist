package com.choongang.todolist.service;

import com.choongang.todolist.dao.TodoDao;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.PageResponse;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoUpdateRequestDto;
import com.choongang.todolist.exception.TodoOwnershipException;
import lombok.RequiredArgsConstructor;
import com.choongang.todolist.dto.TodoListSelectDto;
import com.choongang.todolist.dto.TodoSearchCond;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;


    @Override
    public Todo findById(Long id) {
        return todoDao.findTodoById(id);
    }

    @Override
    @Transactional
    public Todo createTodo(TodoCreateRequestDto todoCreateRequestDto, Long userId) {
        Todo todo = Todo.createTodo(todoCreateRequestDto, userId);
        // todoDao에서 생성된 Todo가 있다면 리턴해주세요.
        return todoDao.saveTodo(todo);
    }

    @Override
    @Transactional
    public Todo updateTodo(TodoUpdateRequestDto todoUpdateRequestDto, Long id, Long userId) {
        Todo todo = todoDao.findTodoById(id);
        if (!todo.getUserId().equals(userId)) {
            throw new TodoOwnershipException("Todo owner is unauthorized");
        }
        todo.setTitle(todoUpdateRequestDto.getTitle());
        todo.setContent(todoUpdateRequestDto.getContent());
        todo.setPriority(todoUpdateRequestDto.getPriority());
        todo.setStatus(todoUpdateRequestDto.getStatus());
        todo.setDueAt(todoUpdateRequestDto.getDueAt());
        todo.setUpdatedAt(LocalDateTime.now());
        return todoDao.updateTodo(todo);
    }


    /**
     * 컨트롤러에서 간단 조회용으로 호출 (cond 기본값 사용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<TodoListSelectDto> retrieveTodos(Long userId) {
        TodoSearchCond cond = new TodoSearchCond(); // page=1,size=10,sort=createdAt,dir=desc …
        PageResponse<TodoListSelectDto> page = retrieveTodos(userId, cond);
        return page.getContent();
    }

    /**
     * 필터/정렬/페이징 전체 처리
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<TodoListSelectDto> retrieveTodos(Long userId, TodoSearchCond cond) {
        List<Todo> rows = todoDao.selectTodosByUser(userId, cond);
        long total = todoDao.countTodosByUser(userId, cond);

        List<TodoListSelectDto> items = rows.stream()
                .map(t -> TodoListSelectDto.builder()
                        .id(t.getTodoId())          // ← 엔티티 필드명에 맞춤
                        .title(t.getTitle())
                        .status(t.getStatus())
                        .dueAt(t.getDueAt())
                        .ddayLabel(computeDdayLabel(t.getDueAt()))
                        .createdAt(t.getCreatedAt())
                        .build()
                )
                .toList();

        return new PageResponse<TodoListSelectDto>(items, cond.getPage(), cond.getSize(), total);
    }

    /**
     * D-day 계산
     */
    private String computeDdayLabel(LocalDateTime dueAt) {
        if (dueAt == null) return "-";
        LocalDate today = LocalDate.now();
        LocalDate due = dueAt.toLocalDate();
        long diff = java.time.temporal.ChronoUnit.DAYS.between(today, due);
        if (diff == 0) return "D-Day";
        return diff > 0 ? "D-" + diff : "D+" + Math.abs(diff);
    }

    @Override
    public boolean deleteTodo(Long todoId) {
        int rows = todoDao.deleteTodo(todoId);

        return rows > 0;
    }
}
