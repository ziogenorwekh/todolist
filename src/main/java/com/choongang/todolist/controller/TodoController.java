package com.choongang.todolist.controller;

import com.choongang.todolist.service.TodoService;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;


import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.domain.TodoStatus;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // postMapping에서 어디에 보낼지를 uri 지정을 하셔야 합니다.
    @PostMapping("/createTodo")
    public String createTodo(@Valid @ModelAttribute TodoCreateRequestDto todoCreateRequestDto,
                             HttpSession session, Model model, BindingResult bindingResult) {
        // Dto의 값을 바인딩하고, 그 과정에서 실제로 null이 되면 안되는 값에 null이 있다면 에러를 보내주셔야 합니다.
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/createTodo";
        }
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
        model.addAttribute("todo", todo);
        // 이건 어제 내가 잘못 설명했네요. id값을 상세페이지에서  처리해야 할 문제입니다. 지금은 생성된 todo를 상세페이지로 옮겨주는
        // 역할만 할 것이에요.
        return "/todo/detail";
    };

    @GetMapping("/createTodo")
    public String createTodo(Model model) {
        model.addAttribute("todoCreateRequestDto", new TodoCreateRequestDto());
        return "/todo/createTodo";
    }
    @GetMapping("/todo/detail/{id}")
    public String detail(@PathVariable Long id, Model model,HttpSession session) {
    	Long todoUserid = todoService.findById(id).getUserId();
    	Object user = session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "로그인을 하고 이용하세요.");
            return "redirect:/";
        }
        Long userId = null;
        if (user instanceof User) {
            userId = ((User) user).getUserId();
        }
        if (!todoUserid.equals(userId)) {
			return "404";
		}
    	Todo todo = todoService.findById(id);
    	model.addAttribute("todo", todo);
        return "/todo/detail";
    }
    @PostMapping("/todo/detail/{id}")
    public String confirm(@PathVariable Long id, HttpSession session, Model model) {
    	Long todoUserid = todoService.findById(id).getUserId();
    	Object user = session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "로그인을 하고 이용하세요.");
            return "redirect:/";
        }
        Long userId = null;
        if (user instanceof User) {
            userId = ((User) user).getUserId();
        }
        if (!todoUserid.equals(userId)) {
			return "404";
		}
    	Todo todo = todoService.findById(id);
    	if (todo.getStatus().equals(TodoStatus.TODO)) {
			todo.setUpdatedAt(LocalDateTime.now());
		} else if (!todo.getStatus().equals(TodoStatus.DONE)) {
			todo.setCompletedAt(LocalDateTime.now());
		}
    	return "/todo/detail";
    }
}
