package com.choongang.todolist.controller;

import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.LoginForm;
import com.choongang.todolist.service.LoginService;
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

    /***
     * 로그인을 하기 위한 페이지로 이동할거에요.
     * @return 로그인 페이지로 이동할거에요.
     */
    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }
    /***
     * 로그인을 처리할거에요. 로그인 페이지에는 로그인을 하기 위한 유저 이메일과, 유저
     * 비밀번호를 입력해야 해요. 그것들을 입력하면 이 메서드에서 로그인을 하기 위한 로직을 처리할거에요.
     * @param loginForm 로그인을 하기 위한, 이메일과 비밀번호를 입력할거에요,
     * @param session 로그인이 성공한다면, 세션에 로그인 한 유저의 정보를 담을거에요.
     * @return 로그인이 성공한다면, 홈으로 이동할거에요. 로그인이 실패한다면, 다시 로그인 페이지로 이동할거에요.
     * 다만 실패하면 error 라는 쿼리 파라미터를 붙여서 이동할거에요.
     */
    @PostMapping("/login")
    public String loginPost(@ModelAttribute LoginForm loginForm, HttpSession session) {
        if (loginForm.getEmail() == null || loginForm.getPassword() == null) {
            return "redirect:/login?error";
        }
        User user = loginService.login(loginForm.getEmail(), loginForm.getPassword());
        session.setAttribute("user", user);
        return "redirect:/";
    }
}
