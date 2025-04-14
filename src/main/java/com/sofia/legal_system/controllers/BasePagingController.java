package com.sofia.legal_system.controllers;

import com.sofia.legal_system.viewmodels.BasePagingFilterViewModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BasePagingController {
    protected void initPaging(Pagination pagination, ComboBox<Integer> sizes, BasePagingFilterViewModel pagingFilterViewModel, Consumer<BasePagingFilterViewModel.PagingEvent> eventSupplier) {
        pagination.maxPageIndicatorCountProperty().bind(pagingFilterViewModel.getMaxPageIndicator());
        pagination.pageCountProperty().bind(pagingFilterViewModel.getTotalPages());
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            pagingFilterViewModel.getPage().set(newValue.intValue() + 1);
        });

        sizes.setItems(pagingFilterViewModel.getSizes());
        pagingFilterViewModel.getPageSize().bind(sizes.getSelectionModel().selectedItemProperty());

        sizes.getSelectionModel().selectFirst();

        pagingFilterViewModel.getPage().addListener((observable, oldValue, newValue) -> {
            eventSupplier.accept(new BasePagingFilterViewModel.PagingEvent(pagingFilterViewModel.getPage().get(), pagingFilterViewModel.getPageSize().get()));
        });
        pagingFilterViewModel.getPageSize().addListener((observable, oldValue, newValue) -> {
            eventSupplier.accept(new BasePagingFilterViewModel.PagingEvent(pagingFilterViewModel.getPage().get(), pagingFilterViewModel.getPageSize().get()));
        });

    }
}
