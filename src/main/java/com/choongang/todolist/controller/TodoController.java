package com.choongang.todolist.controller;

import com.choongang.todolist.dto.TodoUpdateRequestDto;
import com.choongang.todolist.service.TodoService;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.choongang.todolist.domain.Todo;
import com.choongang.todolist.domain.TodoStatus;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.PageResponse;
import com.choongang.todolist.dto.TodoCreateRequestDto;
import com.choongang.todolist.dto.TodoListSelectDto;
import com.choongang.todolist.dto.TodoSearchCond;

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
    
    /** 로그인 사용자 To-do 리스트 조회 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/todos")
    public String list(@AuthenticationPrincipal(expression = "username") Long userId,
                       @RequestParam(required = false) TodoStatus status,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueFrom,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueTo,
                       @RequestParam(defaultValue = "createdAt") String sort,
                       @RequestParam(defaultValue = "desc") String dir,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        TodoSearchCond cond = new TodoSearchCond();
        cond.setStatus(status);
        cond.setKeyword(keyword);
        cond.setDueFrom(dueFrom);
        cond.setDueTo(dueTo);
        cond.setSort(sort);
        cond.setDir(dir);
        cond.setPage(page);
        cond.setSize(size);

        PageResponse<TodoListSelectDto> pageRes = todoService.retrieveTodos(userId, cond);

        model.addAttribute("page", pageRes);
        model.addAttribute("cond", cond);
        model.addAttribute("statuses", TodoStatus.values()); // 필터 드롭다운용
        return "todos/list";
    }

    @GetMapping("/updateTodo")
    public String updateTodo(Model model) {
        model.addAttribute("todoUpdateRequestDto", new TodoUpdateRequestDto());
        return "/todo/updateTodo";
    }

    @PostMapping("/updateTodo")
    public String updateTodo(@Valid @ModelAttribute TodoUpdateRequestDto todoUpdateRequestDto,
                             Long todoId, HttpSession session, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/updateTodo";
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


        todoService.updateTodo(todoUpdateRequestDto, userId, todoId);

        return "/todo/detail";
    }

    @GetMapping("/todo/detail/{id}")
    public String detail(@PathVariable Long id, Model model,HttpSession session) {
    	Long todoUserid = todoService.findById(id).getUserId();
    	Object user = session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "로그인을 하고 이용하세요.");
            return "redirect:/";
        }
        Long userId = ((User) user).getUserId();

        if (!todoUserid.equals(userId)) {
			return "404";
		}
    	Todo todo = todoService.findById(id);
    	model.addAttribute("todo", todo);
        return "/todo/detail";
    }
    @GetMapping("/detail/start")
    public String start(@RequestParam Long id, HttpSession session, Model model) {
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
		} 
    	return "/todo/detail";
    }
    @GetMapping("/detail/done")
    public String done(@RequestParam Long id, HttpSession session, Model model) {
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
    	if (!todo.getStatus().equals(TodoStatus.DONE)) {
			todo.setCompletedAt(LocalDateTime.now());
		} 
    	return "/todo/detail";
    }
}
