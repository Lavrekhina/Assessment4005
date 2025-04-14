package com.sofia.legal_system.model;

import java.util.Collection;

public class Page<T> {
    private final Collection<T> content;

    private final int page;
    private final int pageSize;
    private final int total;
    private final int totalPages;

    public Page(Collection<T> content, int page, int pageSize, int total) {
        this.content = content;
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Collection<T> getContent() {
        return content;
    }
}
