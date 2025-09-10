package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCreateRequestDto {

    // 제약 조건에는 메세지를 담아줄 수 있어요.
    // 다음과 같이 애너테이션에 message라는 메서드가 있어요. 이걸 활용하여 사용자에게 어떤 문제가 일어났는지 보여줄 수 있습니다.
    @NotBlank(message = "타이틀은 필수입니다..")
    private String title;
    private String content;
    @NotBlank
    private Priority priority;
    @NotBlank
    private TodoStatus status;
    @NotBlank
    private LocalDateTime dueAt;

}
