package com.choongang.todolist.user;

import com.choongang.todolist.dao.UserDaoImpl;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {



    private static final PasswordEncoder PasswordEncoder = null;    // UserService에서 매게변수 추가로 인한 수정 JHE

    private UserService userService;

    @InjectMocks
    private UserDaoImpl userDao;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setCreateAt(LocalDateTime.now());
        testUser.setUpdateAt(LocalDateTime.now());

        // 각 테스트가 시작하기 전에 무슨 작업을 실행할지 정하는 메서드에요.
        // 이와 반대로 @BeforeAll은 모든 테스트가 시작하기 전에 딱 한 번만 실행돼요.
    }
    //---------------------------------------------------------

        @Test
    @DisplayName("사용자 이름으로 사용자를 성공적으로 찾아야 한다")
    public void testFindByUsernameSuccess() {
        // Mocking: jdbcTemplate이 queryForObject 호출 시 testUser 객체를 반환하도록 설정
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("testuser")))
                .thenReturn(testUser);

        // 테스트 실행
        Optional<User> foundUser = userDao.findByUsername("testuser");

        // 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    //---------------------------------------------------------

    @Test
    @DisplayName("존재하지 않는 사용자 이름으로 조회 시 Optional.empty()를 반환해야 한다")
    public void testFindByUsernameNotFound() {
        // Mocking: jdbcTemplate이 queryForObject 호출 시 예외를 발생시키도록 설정
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("nonexistent")))
                .thenThrow(new EmptyResultDataAccessException(1));

        // 테스트 실행
        Optional<User> foundUser = userDao.findByUsername("nonexistent");

        // 검증
        assertThat(foundUser).isEmpty();
    }
}
