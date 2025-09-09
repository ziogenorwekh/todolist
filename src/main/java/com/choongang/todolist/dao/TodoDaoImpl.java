package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Todo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final JdbcTemplate jdbcTemplate;

    public TodoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        String sql = "insert into Todos (userId, title, content, priority, " +
                "status, dueAt, createdAt, updateAt, completedAt) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        아래의 주석을 보고 참고하세요.
//        키 홀더는 database에서 auto_increment에서 자동으로 생성한 인덱스 값을 가져다주는 전달자 역할을 해요.
//        PreparedStatement는 전통적으로 SQL 인젝션에 대해 방어하는 코드로 사용하곤 했습니다.
//        입력 파라미터에 SQL 쿼리를 쿼리로 처리하지 않고 문자로 처리합니다.
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setLong(1, todo.getUserId());
//            ps.setString(2, todo.getTitle());
//            ps.setString(3, todo.getContent());
//            ps.setString(4, todo.getPriority().name());
//            ps.setString(5, todo.getStatus().name());
//            ps.setTimestamp(6, Timestamp.valueOf(todo.getDueAt()));
//            ps.setTimestamp(7, Timestamp.valueOf(todo.getCreatedAt()));
//            ps.setTimestamp(8, Timestamp.valueOf(todo.getUpdatedAt()));
//            ps.setTimestamp(9, Timestamp.valueOf(todo.getCompletedAt()));
//            return ps;
//        }, keyHolder);
        // 우리는 키 홀더에서 생성된 key 값을 todo에 다시 세팅해서 리턴할 거에요.
//        todo.setTodoId(keyHolder.getKey().longValue());
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setLong(1, todo.getTodoId());
//            ps.setString(2, todo.getTitle());
//            ps.setString(3, todo.getContent());
//            ps.setString(4, todo.getPriority().name());
//            ps.setString(5, todo.getStatus().name());
//            ps.setTimestamp(6, todo);
//        }, keyHolder);
        jdbcTemplate.update(sql, todo.getUserId(), todo.getTitle(), todo.getContent(), todo.getPriority(),
                todo.getStatus(), todo.getDueAt(), todo.getCreatedAt(), todo.getUpdatedAt(), todo.getCompletedAt());
        return todo;
    }

    /**
     * @param id 우리는 id 값을 Long으로 저장했기 때문에, String이 아닌 Long으로 받아야  합니다.
     * @return
     */
    @Override
    public Todo findTodoById(Long id) {
        String sql = "select * from Todos where id = ?";
        // 아래의 주석을 보고 참조해주세요.
//        try {
//            Todo todo = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
//            return todo;
//        } catch (/*데이터베이스에서 조회 했을 때 결과가 존재하지 않는다면 해당 에러값을 던집니다.*/EmptyResultDataAccessException e) {
//            return null;
//        }
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Todo updateTodo(Todo todo) {
        String sql = "update todos";
        return null;
    }
}
