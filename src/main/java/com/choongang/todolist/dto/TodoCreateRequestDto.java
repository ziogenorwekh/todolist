package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCreateRequestDto {

    @NotBlank(message = "제목은 반드시 입력해야 합니다.")
    private String title;

    @NotBlank(message = "내용은 반드시 입력해야 합니다.")
    private String content;

    @NotNull(message = "우선순위를 선택해주세요.")
    private Priority priority;

    @NotNull(message = "상태를 선택해주세요.")
    private TodoStatus status;

    @NotNull(message = "마감일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dueAt = LocalDateTime.now().plusDays(1);
}
