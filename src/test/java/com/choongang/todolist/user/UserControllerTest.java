package com.choongang.todolist.user;

import com.choongang.todolist.controller.UserController;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.dto.UserUpdateDto;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import com.choongang.todolist.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  ===== UserController Test =====
 * GET /register → 회원가입 폼 뷰 반환
 * POST /register → 입력값 정상 → userService.createUser() 호출 + 리다이렉트
 * POST /register → 비밀번호 불일치 시 에러 메시지 반환
 * POST /register → 중복 이메일 예외 시 에러 메시지 반환
 * GET /delete-account → 로그인 안 됐으면 로그인으로 리다이렉트
 * POST /delete-account → 정상 탈퇴 시 세션 무효화 + 리다이렉트
 * POST /delete-account → 비밀번호 틀리면 에러 메시지 반환
 * 
 */


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper om;

    @Test
    @DisplayName("GET /register → 회원가입 폼 뷰 반환")
    void showRegisterForm_ok() throws Exception {
        mvc.perform(get("/register"))
           .andExpect(status().isOk())
           .andExpect(view().name("auth/register"));
    }

    @Test
    @DisplayName("POST /register → 정상 입력시 회원가입 후 리다이렉트")
    void registerUser_success() throws Exception {
        mvc.perform(post("/register")
                .param("email", "a@a.com")
                .param("password", "pw1234")
                .param("confirmPassword", "pw1234")
                .param("name", "Haeun"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login?success=registered"));

        verify(userService).createUser(any(UserCreateRequestDto.class));
    }

    @Test
    @DisplayName("POST /register → 비밀번호 불일치 시 에러 반환")
    void registerUser_passwordMismatch() throws Exception {
        mvc.perform(post("/register")
                .param("email", "a@a.com")
                .param("password", "pw1")
                .param("confirmPassword", "pw2")
                .param("name", "Haeun"))
           .andExpect(status().isOk())
           .andExpect(view().name("auth/register"))
           .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("POST /register → 중복 이메일 예외 처리")
    void registerUser_duplicateEmail() throws Exception {
        doThrow(new DuplicateEmailException("중복")).when(userService).createUser(any());

        mvc.perform(post("/register")
                .param("email", "dup@a.com")
                .param("password", "pw")
                .param("confirmPassword", "pw")
                .param("name", "dup"))
           .andExpect(status().isOk())
           .andExpect(view().name("auth/register"))
           .andExpect(model().attribute("error", "이미 존재하는 이메일입니다"));
    }

    @Test
    @DisplayName("GET /delete-account → 로그인 안 했으면 로그인으로 리다이렉트")
    void showDeleteForm_notLoggedIn() throws Exception {
        mvc.perform(get("/delete-account"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("POST /delete-account → 비밀번호 맞으면 계정 삭제 후 리다이렉트")
    void deleteUser_success() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User(1L, "haeun", "pw", "a@a.com", null, null));

        mvc.perform(post("/delete-account")
                .param("password", "pw")
                .session(session))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/?deleted=success"));

        verify(userService).deleteUser(eq(1L), eq("pw"));
    }

    @Test
    @DisplayName("POST /delete-account → 비밀번호 틀리면 에러 메시지 반환")
    void deleteUser_wrongPassword() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User(1L, "haeun", "pw", "a@a.com", null, null));

        doThrow(new InvalidPasswordException("비번 불일치"))
                .when(userService).deleteUser(eq(1L), eq("wrong"));

        mvc.perform(post("/delete-account")
                .param("password", "wrong")
                .session(session))
           .andExpect(status().isOk())
           .andExpect(view().name("auth/delete-account"))
           .andExpect(model().attributeExists("error"));
    }
}
