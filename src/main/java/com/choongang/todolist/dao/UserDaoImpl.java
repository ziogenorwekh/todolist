package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertUser(User user) {
        String sql = "INSERT INTO users (email, password, name, created_at) VALUES (?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getCreateAt()
        );
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteUser(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public int saveUser(User user) {
        return insertUser(user);
    }

    // RowMapper 구현
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("name"));

            // created_at 처리 (null 체크)
            if (rs.getTimestamp("created_at") != null) {
                user.setCreateAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            return user;
        }
    };
}