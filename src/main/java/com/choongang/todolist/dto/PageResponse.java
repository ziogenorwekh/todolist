package com.choongang.todolist.dto;

import lombok.Value;

import java.util.List;

@Value
public class PageResponse<T> {
    List<T> content;
    int page;
    int size;
    long totalElements;
    int totalPages;
//    boolean hasPrev;
//    boolean hasNext;

    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        
        int pages = (int)Math.ceil((double) totalElements / size);
        this.totalPages = (pages < 1) ? 1 : pages;
//        this.hasPrev = page > 0;
//        this.hasNext = page + 1 < totalPages;
    }

    public boolean isHasNext() {
        return page + 1 < totalPages;
    }

    public boolean isHasPrev() {
        return page > 0;
    }
}
