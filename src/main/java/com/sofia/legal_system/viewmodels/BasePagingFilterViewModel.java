package com.sofia.legal_system.viewmodels;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BasePagingFilterViewModel {
    private final SimpleIntegerProperty page = new SimpleIntegerProperty(1);
    private final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(10);
    private final SimpleIntegerProperty maxPageIndicator = new SimpleIntegerProperty(10);
    private final SimpleIntegerProperty totalPages = new SimpleIntegerProperty();
    private final ObservableList<Integer> sizes = FXCollections.observableArrayList(5, 10, 20, 50, 100);


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

    public ObservableList<Integer> getSizes() {
        return sizes;
    }

    protected String toSqlPage() {
        return String.format("limit %s offset %s", pageSize.getValue(), (page.getValue() - 1) * pageSize.getValue());
    }

    public record PagingEvent(int page, int size){}
}
