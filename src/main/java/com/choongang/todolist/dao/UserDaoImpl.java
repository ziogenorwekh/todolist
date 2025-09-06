package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*
    * 이메일로 유저를 찾을거에요.
    * 1. 쿼리문을 작성할거에요.
    * 2. jdbcTemplate 을 이용해서 쿼리문을 실행할거에요.
    * 3. 결과를 User 객체로 매핑할거에요.
    * 4. User 객체를 반환할거에요.
     */
    @Override
    public User findUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setCreateAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdateAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return user;
        }, email);
    }

    /**
    * 유저를 저장할거에요.
     * 1. 쿼리문을 작성할거에요.
     * 2. jdbcTemplate 을 이용해서 쿼리문을 실행할거
     * 요.
     * 3. 유저 객체를 저장할거에요.
     * @param user 저장할 유저 객체에요.
     */
    @Override
    public void saveUser(User user) {
        String sql = "insert into users (name, email, password, created_at, updated_at) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(),
                user.getCreateAt(), user.getUpdateAt());

    }

}
