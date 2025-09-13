package com.choongang.todolist.user;

import com.choongang.todolist.dao.UserDaoImpl;
import com.choongang.todolist.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * ===== UserDaoImpl Test =====
 * insertUser → 새 User 저장 후 row count = 1 확인
 * findByEmail, findById, findByUsername → 방금 insert 한 User를 다시 조회
 * updateProfile, updatePassword → 특정 필드 변경 후 값이 바뀌었는지 확인
 * deleteUser → 삭제 후 count 확인
 */

@JdbcTest
@Import(UserDaoImpl.class)   // UserDaoImpl 등록
public class UserDaoImplTest {
	
	    @Autowired
	    private UserDaoImpl userDao;

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    @Test
	    @DisplayName("insertUser: 사용자 저장 성공")
	    void insertUser_ok() {
	        User user = User.builder()
	                .username("haeun")
	                .password("pw1234")
	                .email("test@example.com")
	                .createAt(LocalDateTime.now())
	                .build();

	        int rows = userDao.insertUser(user);

	        assertThat(rows).isEqualTo(1);

	        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
	        assertThat(count).isEqualTo(1L);
	    }

	    @Test
	    @DisplayName("findByEmail: 저장된 사용자 조회")
	    void findByEmail_ok() {
	        jdbcTemplate.update("INSERT INTO users (email, password, name, created_at) VALUES (?,?,?,?)",
	                "a@a.com", "pw", "Haeun", LocalDateTime.now());

	        User found = userDao.findByEmail("a@a.com");

	        assertThat(found).isNotNull();
	        assertThat(found.getEmail()).isEqualTo("a@a.com");
	        assertThat(found.getUsername()).isEqualTo("Haeun");
	    }

	    @Test
	    @DisplayName("findByEmail: 존재하지 않으면 null")
	    void findByEmail_notFound_returnsNull() {
	        User found = userDao.findByEmail("none@a.com");
	        assertThat(found).isNull();
	    }

	    @Test
	    @DisplayName("findByUsername: Optional 조회")
	    void findByUsername_ok() {
	        jdbcTemplate.update("INSERT INTO users (username, password, email, create_at) VALUES (?,?,?,?)",
	                "haeun", "pw", "b@a.com", LocalDateTime.now());

	        Optional<User> opt = userDao.findByUsername("haeun");

	        assertThat(opt).isPresent();
	        assertThat(opt.get().getEmail()).isEqualTo("b@a.com");
	    }

	    @Test
	    @DisplayName("updateProfile: 표시명/이메일/이미지 업데이트")
	    void updateProfile_ok() {
	        jdbcTemplate.update("INSERT INTO users (id, username, password, email, created_at) VALUES (?,?,?,?,?)",
	                1L, "oldName", "pw", "old@a.com", LocalDateTime.now());

	        int rows = userDao.updateProfile(1L, "newName", "new@a.com", "http://img.png");

	        assertThat(rows).isEqualTo(1);

	        String email = jdbcTemplate.queryForObject("SELECT email FROM users WHERE id = 1", String.class);
	        assertThat(email).isEqualTo("new@a.com");
	    }

	    @Test
	    @DisplayName("updatePassword: 비밀번호 변경")
	    void updatePassword_ok() {
	        jdbcTemplate.update("INSERT INTO users (id, username, password, email, created_at) VALUES (?,?,?,?,?)",
	                2L, "haeun", "oldPw", "c@a.com", LocalDateTime.now());

	        int rows = userDao.updatePassword(2L, "ENC_newPw");

	        assertThat(rows).isEqualTo(1);

	        String pw = jdbcTemplate.queryForObject("SELECT password_hash FROM users WHERE id = 2", String.class);
	        assertThat(pw).isEqualTo("ENC_newPw");
	    }

	    @Test
	    @DisplayName("deleteUser: 삭제 성공")
	    void deleteUser_ok() {
	        jdbcTemplate.update("INSERT INTO users (id, username, password, email, created_at) VALUES (?,?,?,?,?)",
	                3L, "delUser", "pw", "d@a.com", LocalDateTime.now());

	        int rows = userDao.deleteUser(3L);
	        assertThat(rows).isEqualTo(1);

	        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE id = 3", Long.class);
	        assertThat(count).isEqualTo(0L);
	    }
}
