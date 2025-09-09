package com.choongang.todolist.controller;

import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.service.UserService;
import com.choongang.todolist.exception.UserNotFoundException;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    // 회원가입을 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserCreateRequestDto userCreateRequestDto, Model model) {
        try {
            // 유효성 검증
            if (userCreateRequestDto.getEmail() == null || userCreateRequestDto.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "이메일을 입력해주세요");
                return "auth/register";
            }

            if (userCreateRequestDto.getPassword() == null || userCreateRequestDto.getPassword().trim().isEmpty()) {
                model.addAttribute("error", "비밀번호를 입력해주세요");
                return "auth/register";
            }

            if (userCreateRequestDto.getName() == null || userCreateRequestDto.getName().trim().isEmpty()) {
                model.addAttribute("error", "이름을 입력해주세요");
                return "auth/register";
            }

            if (!userCreateRequestDto.getPassword().equals(userCreateRequestDto.getConfirmPassword())) {
                model.addAttribute("error", "비밀번호가 일치하지 않습니다");
                return "auth/register";
            }

            // 회원가입 처리
            userService.createUser(userCreateRequestDto);

            return "redirect:/login?success=registered";

        } catch (DuplicateEmailException e) {
            model.addAttribute("error", "이미 존재하는 이메일입니다");
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입에 실패했습니다: " + e.getMessage());
            return "auth/register";
        }
    }

    // 회원 탈퇴 페이지 표시
    @GetMapping("/delete-account")
    public String showDeleteForm(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "auth/delete-account";
    }

    // 회원 탈퇴 처리
    @PostMapping("/delete-account")
    public String deleteUser(@RequestParam String password, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            userService.deleteUser(user.getUserId(), password);
            session.invalidate();
            return "redirect:/?deleted=success";

        } catch (InvalidPasswordException e) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "auth/delete-account";
        } catch (Exception e) {
            model.addAttribute("error", "회원 탈퇴에 실패했습니다: " + e.getMessage());
            return "auth/delete-account";
        }
    }
}