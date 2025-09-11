package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class TodoDaoImpl implements TodoDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Todo saveTodo(Todo todo) {
        String sql = "insert into Todos (userId, title, content, priority, " +
                "status, dueAt, createdAt, updateAt, completedAt) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        아래의 주석을 보고 참고하세요.
//        키 홀더는 database에서 auto_increment에서 자동으로 생성한 인덱스 값을 가져다주는 전달자 역할을 해요.
//        PreparedStatement는 전통적으로 SQL 인젝션에 대해 방어하는 코드로 사용하곤 했습니다.
//        입력 파라미터에 SQL 쿼리를 쿼리로 처리하지 않고 문자로 처리합니다.
//        todo.setTodoId(keyHolder.getKey().longValue());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, todo.getUserId());
            ps.setString(2, todo.getTitle());
            ps.setString(3, todo.getContent());
            ps.setString(4, todo.getPriority().name());
            ps.setString(5, todo.getStatus().name());
            ps.setTimestamp(6,Timestamp.valueOf(todo.getDueAt()));
            ps.setTimestamp(7,Timestamp.valueOf(LocalDateTime.now()));
            //처음 생성할 때 업데이트 날짜를 생성날짜로 할지 Null로 할지 결정
            ps.setTimestamp(8,Timestamp.valueOf(LocalDateTime.now()));
            //완료날짜를 눌로 받아야 한다는 소리가 있음 눌일 경우 NPE오류 발생
            ps.setTimestamp(9,Timestamp.valueOf(todo.getCompletedAt()));
            return ps;
        }, keyHolder);
        todo.setTodoId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return todo;
    }

    /**
     * @param id 우리는 id 값을 Long으로 저장했기 때문에, String이 아닌 Long으로 받아야  합니다.
     * @return
     */
    @Override
    public Todo findTodoById(Long id) {
        String sql = "select * from Todos where todoId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Todo updateTodo(Todo todo) {
        String sql = "UPDATE Todos SET userId = ?, title = ?, content = ?, priority = ?, " +
                "status = ?, dueAt =?, updateAt = ?, completedAt =? WHERE todoId = ?";
        jdbcTemplate.update(sql,
                todo.getUserId(),
                todo.getTitle(),
                todo.getContent(),
                todo.getPriority().name(),
                todo.getStatus().name(),
                todo.getDueAt(),
                LocalDateTime.now(),
                todo.getCompletedAt(),
                todo.getTodoId()
                );
        return todo;

    }
}
