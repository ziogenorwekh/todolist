package com.choongang.todolist.controller;

import com.choongang.todolist.config.security.CustomUserDetails;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.dto.UserUpdateDto;
import com.choongang.todolist.service.UserService;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete-account")
    public String showDeleteForm() {
        return "user/delete-account";
    }

    // 회원 탈퇴 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete-account")
    public String deleteUser(@RequestParam String password,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        try {
            userService.deleteUser(userDetails.getId(), password);
            return "redirect:/?deleted=success";
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "user/delete-account";
        } catch (Exception e) {
            model.addAttribute("error", "회원 탈퇴에 실패했습니다: " + e.getMessage());
            return "user/delete-account";
        }
    }
    
    
    /** 수정 폼 진입 (본인) */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me/edit")
    public String editMe(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserUpdateDto form = new UserUpdateDto();
        model.addAttribute("form", form);
        return "user/edit";
    }

    /** 수정 제출 (본인) */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/me/edit")
    public String updateMe(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @Valid @ModelAttribute("form") UserUpdateDto form,
                           BindingResult binding) {
        if (binding.hasErrors()) {
            return "user/edit";
        }

        try {
            Long userId = userDetails.getId();
            userService.updateUser(userId, userId, form);
            return "redirect:/mypage";
        } catch (IllegalArgumentException e) {
            binding.rejectValue("newPasswordConfirm", "mismatch", e.getMessage());
            return "user/edit";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {
        Long userId = userDetails.getId();

        return "user/mypage";
    }

    
}