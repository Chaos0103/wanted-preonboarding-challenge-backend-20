package com.wanted.challenge.api;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int currentPage;
    private final int size;
    private final Boolean isFirst;
    private final Boolean isLast;

    private PageResponse(Page<T> data) {
        this.content = data.getContent();
        this.currentPage = data.getNumber() + 1;
        this.size = data.getSize();
        this.isFirst = data.isFirst();
        this.isLast = data.isLast();
    }

    public static <T> PageResponse<T> of(Page<T> data) {
        return new PageResponse<>(data);
    }
}
