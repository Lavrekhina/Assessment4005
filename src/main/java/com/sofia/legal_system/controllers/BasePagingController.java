package com.sofia.legal_system.controllers;

import com.sofia.legal_system.model.KeyValuePair;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.BasePagingFilterViewModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;

import java.util.List;

import static utils.Consts.PAGE_SIZES;

public abstract class BasePagingController {
    public ComboBox<KeyValuePair<String, String>> sortFieldDD;
    public ComboBox<KeyValuePair<String, String>> sortOrderDD;

    public ComboBox<Integer> pageSizeDropDown;
    public Pagination pagination;

    protected void initPagingAndSorting(BasePagingFilterViewModel pagingFilterViewModel) {
        pagination.maxPageIndicatorCountProperty().bind(pagingFilterViewModel.getMaxPageIndicator());
        pagination.pageCountProperty().bind(pagingFilterViewModel.getTotalPages());
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            pagingFilterViewModel.getPage().set(newValue.intValue() + 1);
        });

        pageSizeDropDown.setItems(FXCollections.observableList(PAGE_SIZES));
        pagingFilterViewModel.getPageSize().bind(pageSizeDropDown.getSelectionModel().selectedItemProperty());

        pageSizeDropDown.getSelectionModel().selectFirst();

        pagingFilterViewModel.getPage().addListener((observable, oldValue, newValue) -> {
            refreshTable(null);
        });
        pagingFilterViewModel.getPageSize().addListener((observable, oldValue, newValue) -> {
            refreshTable(null);
        });


        sortFieldDD.setItems(FXCollections.observableArrayList(getProps()));
        sortFieldDD.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            pagingFilterViewModel.setSortField(newValue.getValue());
        });
        sortFieldDD.getSelectionModel().selectFirst();

        sortOrderDD.setItems(FXCollections.observableArrayList(List.of(KeyValuePair.of("Descending", "desc"), KeyValuePair.of("Ascending", "asc"))));
        sortOrderDD.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            pagingFilterViewModel.setSortOrder(newValue.getValue());
        });
        sortOrderDD.getSelectionModel().selectFirst();
    }

    public abstract void refreshTable(ActionEvent actionEvent);

    protected abstract List<KeyValuePair<String, String>> getProps();

    public void deleteRows(ActionEvent actionEvent) {
        var result = GUIService.showConfirmationAlert("Delete item", "Are you ok with this?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteSelected();
        }
    }

    protected abstract void deleteSelected();
}
