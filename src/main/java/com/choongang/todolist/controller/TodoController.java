package com.choongang.todolist.controller;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.service.TodoService;
import com.choongang.todolist.service.TodoServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public String createTodo(@Valid @ModelAttribute TodoCreateRequestDto todoCreateRequestDto, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "로그인을 하고 이용하세요.");
            return "redirect:/";
        }
        Long userId = null;
        if (user instanceof User) {
            userId = ((User) user).getUserId();
        }
        Todo todo = todoService.createTodo(todoCreateRequestDto, userId);
        return "/todo/" +todo.getTodoId();
    };

    @GetMapping("/createTodo")
    public String createTodo(Model model) {
        model.addAttribute("todoCreateRequestDto", new TodoCreateRequestDto());
        return "/todo/createTodo";
    }

}
