package com.sofia.legal_system.viewmodels;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class BasePagingFilterViewModel {
    private final SimpleIntegerProperty page = new SimpleIntegerProperty(1);
    private final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(10);
    private final SimpleIntegerProperty maxPageIndicator = new SimpleIntegerProperty(10);
    private final SimpleIntegerProperty totalPages = new SimpleIntegerProperty();
    private String sortField;
    private String sortOrder;


    public SimpleIntegerProperty getPage() {
        return page;
    }

    public SimpleIntegerProperty getPageSize() {
        return pageSize;
    }

    public SimpleIntegerProperty getMaxPageIndicator() {
        return maxPageIndicator;
    }

    public SimpleIntegerProperty getTotalPages() {
        return totalPages;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public abstract String toSqlFilter();

    public String toSort() {
        if (sortField == null || sortOrder == null) {
            return "";
        }

        return "order by " + sortField + " " + sortOrder;
    }
}
