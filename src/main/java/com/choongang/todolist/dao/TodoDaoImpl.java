package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        String sql = "insert into Todos (userId, title, content, priority, " +
                "status, dueAt, createdAt, updateAt, completedAt) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, todo.getUserId(), todo.getTitle(), todo.getContent(), todo.getPriority(),
                todo.getStatus(), todo.getDueAt(), todo.getCreatedAt(), todo.getUpdatedAt(), todo.getCompletedAt());
//        jdbcTemplate.
        return todo;
    }

    @Override
    public Todo findTodoById(String id) {
        String sql = "select * from Todos where id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
    }
}
