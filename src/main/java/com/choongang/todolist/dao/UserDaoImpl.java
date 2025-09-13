package com.choongang.todolist.dao;

import com.choongang.todolist.domain.User;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
//@RequiredArgsConstructor  //final 필드나 @NonNull 필드를 대상으로 생성자를 자동 생성해줌
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertUser(User user) {
        String sql = "INSERT INTO users (email, password, name, created_at) VALUES (?, ?, ?, ?)";

        //        아래의 주석을 보고 참고하세요.
//        키 홀더는 database에서 auto_increment에서 자동으로 생성한 인덱스 값을 가져다주는 전달자 역할을 해요.
//        PreparedStatement는 전통적으로 SQL 인젝션에 대해 방어하는 코드로 사용하곤 했습니다.
//        입력 파라미터에 SQL 쿼리를 쿼리로 처리하지 않고 문자로 처리합니다.
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, user.getEmail());
//            ps.setString(2, user.getPassword());
//            ps.setString(3, user.getUsername());
//            ps.setTimestamp(4, Timestamp.valueOf(user.getCreateAt()));
//            return ps;
//        }, keyHolder);
//        user.setUserId(keyHolder.getKey().longValue());
        // 데이터베이스에서 id 값을 자동 생성해주니, 해당 쿼리를 수행 후, 키 홀더에서 생성된 키값을 세팅하고 유저를
//        리턴하면 좋을 수 있습니다.
        // return user;
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
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /** 프로필(표시명/이메일/프로필이미지)만 수정 JHE */
    public int updateProfile(Long userId, String displayName, String email, String profileImageUrl) {
        String sql = """
            UPDATE users
               SET display_name = :displayName,
                   email = :email,
                   profile_image_url = :profileImageUrl,
                   updated_at = NOW()
             WHERE id = :id
        """;
        var params = new MapSqlParameterSource()
                .addValue("displayName", displayName)
                .addValue("email", email)
                .addValue("profileImageUrl", profileImageUrl)
                .addValue("id", userId);
        return jdbcTemplate.update(sql, params);
    }

    /** 비밀번호만 수정(해시 저장) JHE*/
    public int updatePassword(Long userId, String passwordHash) {
        String sql = """
            UPDATE users
               SET password_hash = :pwd,
                   updated_at = NOW()
             WHERE id = :id
        """;
        var params = new MapSqlParameterSource()
                .addValue("pwd", passwordHash)
                .addValue("id", userId);
        return jdbcTemplate.update(sql, params);
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