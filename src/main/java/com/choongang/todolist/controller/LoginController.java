package com.choongang.todolist.controller;

import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.LoginForm;
import com.choongang.todolist.service.LoginService;
import com.choongang.todolist.exception.UserLoginException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 로그인을 하기 위한 페이지로 이동합니다.
     * @return 로그인 페이지로 이동합니다.
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login"; // 앞의 슬래시(/) 제거
    }

    /**
     * 로그인을 처리합니다. 로그인 페이지에는 로그인을 하기 위한 유저 이메일과, 유저
     * 비밀번호를 입력해야 합니다. 그것들을 입력하면 이 메서드에서 로그인을 하기 위한 로직을 처리합니다.
     * @param loginForm 로그인을 하기 위한, 이메일과 비밀번호를 입력합니다.
     * @param session 로그인이 성공한다면, 세션에 로그인 한 유저의 정보를 담습니다.
     * @param model 에러 메시지를 전달하기 위한 모델입니다.
     * @return 로그인이 성공한다면, 홈으로 이동합니다. 로그인이 실패한다면, 다시 로그인 페이지로 이동합니다.
     */
//    @PostMapping("/login")
//    public String loginPost(@ModelAttribute LoginForm loginForm, HttpSession session, Model model) {
//        // 입력값 검증
//        if (loginForm.getEmail() == null || loginForm.getPassword() == null) {
//            model.addAttribute("error", "이메일과 비밀번호를 입력해주세요.");
//            return "auth/login";
//        }
//
//        try {
//            // 수정: LoginService.login() -> loginService.login()
//            // static 메서드가 아닌 인스턴스 메서드로 호출
//            User user = loginService.login(loginForm.getEmail(), loginForm.getPassword());
//            session.setAttribute("user", user);
//            return "/home";
//
//        } catch (UserLoginException e) {
//            // 로그인 실패 시 에러 메시지와 함께 로그인 페이지로 이동
//            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
//            return "auth/login";
//        }
//    }
}