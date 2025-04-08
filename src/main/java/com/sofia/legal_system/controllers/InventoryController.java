package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.InventoryFilterViewModel;
import com.sofia.legal_system.viewmodels.InventoryViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;

public class InventoryController {
    public Button addBtn;
    public Button deleteBtn;
    public TableView<InventoryViewModel> inventoryTable;

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public TextField nameSearchTextField;
    public TextField locationSearchTextField;
    public TextField qMinField;
    public TextField qMaxField;

    private final InventoryFilterViewModel filterViewModel = new InventoryFilterViewModel();

    @FXML
    public void initialize() {
        TableColumn<InventoryViewModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<InventoryViewModel, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<InventoryViewModel, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<InventoryViewModel, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        inventoryTable.getColumns().add(idColumn);
        inventoryTable.getColumns().add(nameColumn);
        inventoryTable.getColumns().add(quantityColumn);
        inventoryTable.getColumns().add(locationColumn);


        editBtn.disableProperty().bind(inventoryTable.getSelectionModel().selectedItemProperty().isNull());
        deleteBtn.disableProperty().bind(inventoryTable.getSelectionModel().selectedItemProperty().isNull());
        loadingProgressBar.visibleProperty().bind(fetchingEntities);


        nameSearchTextField.textProperty().bindBidirectional(filterViewModel.getNameSearch());
        locationSearchTextField.textProperty().bindBidirectional(filterViewModel.getLocationSearch());
        qMinField.textProperty().bindBidirectional(filterViewModel.getqMin(), new NumberStringConverter());
        qMaxField.textProperty().bindBidirectional(filterViewModel.getqMax(), new NumberStringConverter());

    }

    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            try {
                String filter = filterViewModel.toSqlFilter();  
                return (filter.isEmpty()? inventoryDAO.getAll():inventoryDAO.getAll(filter)).stream().map(i -> new InventoryViewModel(i.getId(), i.getName(), i.getQuantity(), i.getLocation())).toList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(inventoryViewModels -> {
            inventoryTable.getItems().clear();
            inventoryTable.getItems().addAll(inventoryViewModels);
            fetchingEntities.set(false);
        });
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-inventory.fxml");
    }

    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-inventory.fxml", inventoryTable.getSelectionModel().getSelectedItem());
    }

    public void deleteRows(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete item");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteSelected();
        }
    }

    public void deleteSelected() {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            inventoryTable.getSelectionModel().getSelectedItems().stream().map(InventoryViewModel::getId).forEach(integer -> {
                try {
                    inventoryDAO.deleteById(integer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            return true;
        }).thenAccept(inventoryViewModels -> {
            refreshTable(null);
            fetchingEntities.set(false);
        });

    }

    public void filterEntities(ActionEvent actionEvent) {
        refreshTable(null);
    }
}
