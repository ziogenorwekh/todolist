package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, create_at, update_at FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setUserId(rs.getLong("user_id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password"));
                        u.setEmail(rs.getString("email"));
                        u.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                        u.setUpdateAt(rs.getTimestamp("update_at").toLocalDateTime());
                        return u;
                    },
                    username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveUser(User user) {
    }

    @Override
    public Optional<User> findUserById(Long id) {

        return Optional.empty();
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUserById(Long id) {

    }

    @Override
    public void deleteUserByEmail(String email) {

    }
}