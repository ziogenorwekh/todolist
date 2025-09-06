package com.choongang.todolist.advice;

import com.choongang.todolist.exception.UserLoginException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * UserExceptionHandler
 * 사용자 관련 예외를 처리하는 클래스입니다.
 */
@ControllerAdvice
public class UserExceptionHandler {

    /**
     * 로그인 실패 예외 처리
     * 로그인 실패 시 로그인 페이지로 리다이렉트하면서 error 메시지 전달
     */
    @ExceptionHandler(UserLoginException.class)
    public String handleUserLoginException(UserLoginException ex, RedirectAttributes redirectAttributes) {
        // RedirectAttributes로 쿼리 파라미터 또는 플래시 속성 전달 가능
        redirectAttributes.addFlashAttribute("loginError", ex.getMessage());
        return "redirect:/login?error";  // 기존 로그인 페이지 + error 쿼리
    }

    /**
     * 다른 사용자 관련 예외를 잡고 싶으면 추가 가능
     */
}
