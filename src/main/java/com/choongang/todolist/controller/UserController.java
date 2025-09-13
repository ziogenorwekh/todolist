package com.choongang.todolist.controller;

import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.dto.UserUpdateDto;
import com.choongang.todolist.service.UserService;
import com.choongang.todolist.exception.UserNotFoundException;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;


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
    public String registerUser(@ModelAttribute @Valid UserCreateRequestDto userCreateRequestDto,
                               BindingResult bindingResult, Model model) {

        // 자동 유효성 검증 처리
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
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
        return "user/delete-account";
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
    
    
    /** 수정 폼 진입 (본인) */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me/edit")
    public String editMe(@AuthenticationPrincipal(expression = "username") Long userId, Model model) {
        // 보통은 DB에서 내 정보 조회해서 폼에 채움 (여기선 간단히 빈 폼 or 조회결과 세팅)
        UserUpdateDto form = new UserUpdateDto();
        // 예: form.setDisplayName(user.getDisplayName()); ...
        model.addAttribute("form", form);
        return "users/edit";
    }

    /** 수정 제출 (본인) */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/me/edit")
    public String updateMe(@AuthenticationPrincipal(expression = "username") Long userId,
                           @Valid @ModelAttribute("Dto") UserUpdateDto Dto,
                           BindingResult binding,
                           Model model) {
        if (binding.hasErrors()) {
            return "users/edit";
        }
        try {
            userService.updateUser(userId, userId, Dto); // target=actor (본인)
        } catch (IllegalArgumentException e) {
            binding.rejectValue("newPasswordConfirm", "mismatch", e.getMessage());
            return "users/edit";
        }
        // 성공 후 마이페이지 등으로 리다이렉트
        return "redirect:/mypage";
    }
    
}