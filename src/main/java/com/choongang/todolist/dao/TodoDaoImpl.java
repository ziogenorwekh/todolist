package com.choongang.todolist.dao;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.dto.TodoSearchCond;
import lombok.RequiredArgsConstructor;
import com.choongang.todolist.domain.TodoStatus;
import com.choongang.todolist.dto.TodoSearchCond;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoDaoImpl implements TodoDao {

    private final JdbcTemplate jdbcTemplate;

    public TodoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /** 공용 매퍼 : 스프링 JDBC(JdbcTemplate)에서 SQL 실행 결과(ResultSet)을 → 자바 객체로 변환해주는 역할을 하는 게 RowMapper JHE */
    private static final RowMapper<Todo> TODO_MAPPER = (rs, rn) -> {
        Todo t = new Todo();
        t.setTodoId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setTitle(rs.getString("title"));
        t.setContent(rs.getString("content"));
        t.setPriority(Priority.valueOf(rs.getString("priority")));
        t.setStatus(TodoStatus.valueOf(rs.getString("status")));
        Timestamp dueAt = rs.getTimestamp("due_at");
        Timestamp createAt = rs.getTimestamp("created_at");
        Timestamp updateAt = rs.getTimestamp("updated_at");
        Timestamp completedAt = rs.getTimestamp("completed_at");
        t.setDueAt(dueAt == null ? null : dueAt.toLocalDateTime());
        t.setCreatedAt(createAt == null ? null : createAt.toLocalDateTime());
        t.setUpdatedAt(updateAt == null ? null : updateAt.toLocalDateTime());
        t.setCompletedAt(completedAt == null ? null : completedAt.toLocalDateTime());
        return t;
    };
    
    
    @Override
    public Todo saveTodo(Todo todo) {
        String sql = "insert into todos (user_id, title, content, priority, " +
                "status, due_at, created_at, updated_at, completed_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            if (todo.getCompletedAt() != null) {
                ps.setTimestamp(9,Timestamp.valueOf(todo.getCompletedAt()));
            } else {
                ps.setNull(9, java.sql.Types.TIMESTAMP);
            }
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
        String sql = """
        SELECT 
            id AS todoId,
            user_id AS userId,
            title,
            content,
            priority,
            status,
            due_at AS dueAt,
            created_at AS createdAt,
            updated_at AS updatedAt,
            completed_at AS completedAt
        FROM todos
        WHERE id = ?
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Todo.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public Todo updateTodo(Todo todo) {
        String sql = "UPDATE todos SET user_id = ?, title = ?, content = ?, priority = ?, " +
                "status = ?, due_at =?, updated_at = ?, completed_at =? WHERE id = ?";
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
    
    
    /** 로그인 사용자(userId) 소유의 Todo 목록 조회 JHE */
    @Override
    public List<Todo> selectTodosByUser(Long userId, TodoSearchCond cond) {
        // 정렬 화이트리스트
        String sort = (cond != null && cond.getSort() != null) ? cond.getSort() : "created_at";
        switch (sort) {
            case "title": 
            case "due_at": 
            case "priority": 
            case "status": 
            case "created_at": 
            	break;
            default: sort = "created_at";
        }
        String dir = (cond != null && "ASC".equalsIgnoreCase(cond.getDir())) ? "ASC" : "DESC";

        StringBuilder sql = new StringBuilder(
            "SELECT id, user_id, title, content, priority, status, " +
            "       due_at, created_at, updated_at, completed_at " +
            "  FROM todos " +
            " WHERE user_id = ?"
        );
        List<Object> args = new ArrayList<>();
        args.add(userId);

        if (cond != null) {
            if (cond.getKeyword() != null && !cond.getKeyword().isBlank()) {
                sql.append(" AND (title LIKE ? OR content LIKE ?)");
                String kw = "%" + cond.getKeyword().trim() + "%";
                args.add(kw);
                args.add(kw);
            }
            if (cond.getStatus() != null) {
                sql.append(" AND status = ?");
                args.add(cond.getStatus().name());
            }
            if (cond.getPriority() != null) {
                sql.append(" AND priority = ?");
                args.add(cond.getPriority().name());
            }
            if (cond.getDueFrom() != null) {
                sql.append(" AND due_at >= ?");
                args.add(Timestamp.valueOf(cond.getDueFrom().atStartOfDay()));
            }
            if (cond.getDueTo() != null) {
                sql.append(" AND due_at < ?");
                args.add(Timestamp.valueOf(cond.getDueTo().atTime(23, 59, 59)));
            }
        }

        sql.append(" ORDER BY ").append(sort).append(' ').append(dir);
        
        // 페이징
        int size = (cond != null && cond.getSize() > 0) ? cond.getSize() : 10;
        int page = (cond != null && cond.getPage() >= 0) ? cond.getPage() : 0;
        sql.append(" LIMIT ? OFFSET ?");
        args.add(size);
        args.add(page * size);

        return jdbcTemplate.query(sql.toString(), 
        						  TODO_MAPPER,    // new BeanPropertyRowMapper<>(Todo.class) 사용시, enum 같은 타입도 제대로 받아오는지?
        						  args.toArray());
    }

        
        
        /** 사용자(userId) 소유의 조회 결과의 전체 개수 JHE */
    @Override
    public long countTodosByUser(Long userId, TodoSearchCond cond) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM todos WHERE user_id = ?"
        );
        List<Object> args = new ArrayList<>();
        args.add(userId);

        if (cond != null) {
            if (cond.getKeyword() != null && !cond.getKeyword().isBlank()) {
                sql.append(" AND (title LIKE ? OR content LIKE ?)");
                String kw = "%" + cond.getKeyword().trim() + "%";
                args.add(kw);
                args.add(kw);
            }
            if (cond.getStatus() != null) {
                sql.append(" AND status = ?");
                args.add(cond.getStatus().name());
            }
            if (cond.getPriority() != null) {
                sql.append(" AND priority = ?");
                args.add(cond.getPriority().name());
            }
            if (cond.getDueFrom() != null) {
                sql.append(" AND due_at >= ?");
                args.add(Timestamp.valueOf(cond.getDueFrom().atStartOfDay()));
            }
            if (cond.getDueTo() != null) {
                sql.append(" AND due_at < ?");
                args.add(Timestamp.valueOf(cond.getDueTo().atTime(23, 59, 59)));
            }
        }

        Number n = jdbcTemplate.queryForObject(sql.toString(), Number.class, args.toArray());
        return n == null ? 0L : n.longValue();
    }

    @Override
    public int deleteTodo(Long todoId) {
        String sql = "DELETE FROM todos WHERE id = ?";
        return jdbcTemplate.update(sql, todoId);
    }
}
