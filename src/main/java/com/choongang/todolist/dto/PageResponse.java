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
    boolean hasPrev;
    boolean hasNext;

    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int)Math.ceil((double) totalElements / size);
        this.hasPrev = page > 0;
        this.hasNext = page + 1 < totalPages;
    }
}
