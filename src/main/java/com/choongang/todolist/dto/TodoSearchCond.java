package com.choongang.todolist.dto;

import com.choongang.todolist.domain.Priority;
import com.choongang.todolist.domain.TodoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data 				// getter/setter/toString/equals/hashCode 생성
@NoArgsConstructor 	// 기본 생성자
@AllArgsConstructor // 모든 필드 받는 생성자
public class TodoSearchCond {
    private TodoStatus status;       // 상태 필터 
    private Priority priority;       // 우선순위
    private String keyword;          // 제목 키워드 
    private LocalDate dueFrom;       // 마감 시작일 
    private LocalDate dueTo;         // 마감 종료일 

    
    private String sort = "createdAt"; 	// 정렬: createdAt | dueAt | title | status
    private String dir = "desc";		// 정렬 방향: asc | desc

    // 페이징
    private int page = 0;  // 0부터 시작
    private int size = 10; 
    
    // 유틸
    public int getOffset() {
        return page * size;
    }
}
