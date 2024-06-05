package com.wanted.challenge.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

    private static final int PAGE_SIZE = 10;

    public static Pageable generate(int page) {
        return PageRequest.of(page - 1, PAGE_SIZE);
    }
}
