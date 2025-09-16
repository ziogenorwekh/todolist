package com.choongang.todolist.controller;

import com.choongang.todolist.config.security.CustomUserDetails;
import com.choongang.todolist.dto.TodoUpdateRequestDto;
import com.choongang.todolist.exception.TodoOwnershipException;
import com.choongang.todolist.service.TodoService;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.*;


@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // postMapping에서 어디에 보낼지를 uri 지정을 하셔야 합니다.
    @PostMapping("/createTodo")
    public String createTodo(@Valid @ModelAttribute TodoCreateRequestDto todoCreateRequestDto, BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Dto의 값을 바인딩하고, 그 과정에서 실제로 null이 되면 안되는 값에 null이 있다면 에러를 보내주셔야 합니다.
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "todo/createTodo";
        }
        Todo todo = todoService.createTodo(todoCreateRequestDto, userDetails.getId());
        model.addAttribute("todo", todo);
        // 이건 어제 내가 잘못 설명했네요. id값을 상세페이지에서  처리해야 할 문제입니다. 지금은 생성된 todo를 상세페이지로 옮겨주는
        // 역할만 할 것이에요.
        return "redirect:/todo/detail/"+ todo.getTodoId();
    };

    @GetMapping("/createTodo")
    public String createTodo(Model model) {
        model.addAttribute("todoCreateRequestDto", new TodoCreateRequestDto());

        return "todo/createTodo";
    }

    /**
     * 로그인 사용자 To-do 리스트 조회
     */
    // RequestParam으로 받는 방법도 좋습니다.
    // 코드가 길어져서 피곤할 수도 있습니다.
    // RequsetParam으로 받는 파라미터를 DTO화 할 수있어요.
    // HE님이 작성하신 코드는 모두 DTO를 리턴하고 있습니다. 마찬가지로 입력받은 값을 DTO로 받을 수 있습니다.
    // public class TodoSearchRequest {
    //    private TodoStatus status;
    //    private String keyword;
    //    private LocalDate dueFrom;
    //    private LocalDate dueTo;
    //    private String sort = "createdAt";
    //    private String dir = "desc";
    //    private int page = 0;
    //    private int size = 10;
    //} 이와같은 객체를 받고,
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/todos")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails,
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
        cond.setSize(size);PageResponse<TodoListSelectDto> pageRes = todoService.retrieveTodos(userDetails.getId(),
                cond);

        model.addAttribute("page", pageRes);
        model.addAttribute("cond", cond);
        model.addAttribute("statuses", TodoStatus.values()); // 필터 드롭다운용
        return "todo/SelectTodoList";
    }

    @GetMapping("/updateTodo/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
//       Object user = session.getAttribute("user");
       if (userDetails == null) {
           return "redirect:/login";
       }

       Todo todo = todoService.findById(id);

       // Todo가 존재여부 확인 유저아이디 작성자와 같은지 확인
       if (todo == null || !todo.getUserId().equals(userDetails.getId())) {
           return "404";
       }

       model.addAttribute("todo", todo);

       return "todo/updateTodo";
    }

    @PostMapping("/updateTodo/{id}")
    public String updateTodo(@Valid @ModelAttribute("todo") TodoUpdateRequestDto todoUpdateRequestDto,
                             BindingResult bindingResult,
                             @PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("todo", todoUpdateRequestDto); // 폼 값 유지
            return "todo/updateTodo";
        }

        Todo todo = todoService.updateTodo(todoUpdateRequestDto, id, userDetails.getId());
        model.addAttribute("todo", todo);
        return "todo/detail";
    }

    @GetMapping("/todo/detail/{id}")
    public String detail(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
//    	Long todoUserid = todoService.findById(id).getUserId();
//    	Object user = session.getAttribute("user");
//
//        if (user == null) {
//            model.addAttribute("error", "로그인을 하고 이용하세요.");
//            return "redirect:/";
//        }
//        Long userId = ((User) user).getUserId();
        Todo todo = todoService.findById(id);
        Long todoUserId = todo.getUserId();
        if (!todoUserId.equals(user.getId())) {
            return "404";
        }
    	model.addAttribute("todo", todo);
        return "todo/detail";
    }
    @PostMapping("/detail/start/{id}")
    public String start(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        Todo todo = todoService.findById(id);
        todoService.updateDoing(id, user.getId());

    	return "redirect:/todo/detail/" + id;
    }

    @PostMapping("/detail/done/{id}")
    public String done(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
            todoService.updateDone(id, user.getId());
            return "redirect:/todo/detail/" + id;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        boolean deleted = todoService.deleteTodo(id);
        Long todoUserid = todoService.findById(id).getUserId();
        Todo todo = todoService.findById(id);
        Long todoUserId = todo.getUserId();
        if (!todoUserid.equals(todoUserId)) {
            return ResponseEntity.notFound().build();
        }
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
