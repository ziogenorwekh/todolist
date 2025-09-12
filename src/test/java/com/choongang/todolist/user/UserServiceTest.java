package com.choongang.todolist.user;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import com.choongang.todolist.exception.UserNotFoundException;

import com.choongang.todolist.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        // Given: 신규 사용자 DTO
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setConfirmPassword("password");
        dto.setName("Tester");

        // 중복 이메일 존재 안함
        when(userDao.findByEmail(dto.getEmail())).thenReturn(null);
        // insertUser 성공(1건 입력)
        when(userDao.insertUser(any(User.class))).thenReturn(1);

        // When
        User createdUser = userService.createUser(dto);

        // Then
        assertNotNull(createdUser);
        assertEquals(dto.getEmail(), createdUser.getEmail());
        assertEquals(dto.getName(), createdUser.getUsername());
        verify(userDao).findByEmail(dto.getEmail());
        verify(userDao).insertUser(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        UserCreateRequestDto dto = new UserCreateRequestDto();
        dto.setEmail("dup@example.com");
        dto.setPassword("pass");
        dto.setConfirmPassword("pass");
        dto.setName("DupUser");

        // 중복 이메일 존재
        when(userDao.findByEmail(dto.getEmail())).thenReturn(new User());

        // 예외 발생 확인
        assertThrows(DuplicateEmailException.class, () -> userService.createUser(dto));

        verify(userDao).findByEmail(dto.getEmail());
        verify(userDao, never()).insertUser(any());
    }

    @Test
    void deleteUser_Success() {
        Long userId = 1L;
        String password = "pass123";
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        when(userDao.findById(userId)).thenReturn(user);
        when(userDao.deleteUser(userId)).thenReturn(1);

        assertDoesNotThrow(() -> userService.deleteUser(userId, password));

        verify(userDao).findById(userId);
        verify(userDao).deleteUser(userId);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        Long userId = 99L;
        when(userDao.findById(userId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId, "any"));

        verify(userDao).findById(userId);
        verify(userDao, never()).deleteUser(anyLong());
    }

    @Test
    void deleteUser_InvalidPassword_ThrowsException() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setPassword("correctPass");

        when(userDao.findById(userId)).thenReturn(user);

        assertThrows(InvalidPasswordException.class, () -> userService.deleteUser(userId, "wrongPass"));

        verify(userDao).findById(userId);
        verify(userDao, never()).deleteUser(anyLong());
    }

    @Test
    void deleteUser_DeleteFails_ThrowsException() {
        Long userId = 1L;
        String password = "pass";
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        when(userDao.findById(userId)).thenReturn(user);
        when(userDao.deleteUser(userId)).thenReturn(0);  // 실패

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(userId, password));
        assertTrue(exception.getMessage().contains("삭제에 실패"));

        verify(userDao).findById(userId);
        verify(userDao).deleteUser(userId);
    }
}
