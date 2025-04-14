package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.model.KeyValuePair;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.inventory.InventoryFilterViewModel;
import com.sofia.legal_system.viewmodels.inventory.InventoryViewModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.IntegerStringConverter;

public class InventoryController extends BasePagingController {
    public Button addBtn;
    public Button deleteBtn;
    public TableView<InventoryViewModel> inventoryTable;

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public TextField nameSearchTextField;
    public ComboBox<String> locationDropDown;
    public TextField qMinField;
    public TextField qMaxField;

    private final InventoryFilterViewModel filterViewModel = new InventoryFilterViewModel();
    public ComboBox<Integer> pageSizeDropDown;
    public ComboBox<KeyValuePair> sortFieldDD;
    public ComboBox<KeyValuePair> sortOrderDD;
    public Pagination pagination;

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
        qMinField.textProperty().bindBidirectional(filterViewModel.getqMin(), new IntegerStringConverter());
        qMaxField.textProperty().bindBidirectional(filterViewModel.getqMax(), new IntegerStringConverter());
        locationDropDown.setItems(FXCollections.observableArrayList("London", "Manchester"));
        filterViewModel.getLocationSearch().bind(locationDropDown.getSelectionModel().selectedItemProperty());
        sortFieldDD.setItems(FXCollections.observableArrayList(
                new KeyValuePair("ID", "item_id"), 
                new KeyValuePair("Name", "item_name"),
                new KeyValuePair("Quantity", "item_quantity"),
                new KeyValuePair("Location", "item_location")
                ));
        
        sortOrderDD.setItems(FXCollections.observableArrayList(
                new KeyValuePair("Ascending", "asc"), 
                new KeyValuePair("Descending", "desc")           
                ));
    
        initPaging(pagination, pageSizeDropDown, filterViewModel, p -> {
            refreshTable(null);
        });

        refreshTable(null);
    }

    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            String filter = filterViewModel.toSqlFilter();
            return filter.isEmpty()
                    ? inventoryDAO.getAll(filterViewModel.getPage().get(), filterViewModel.getPageSize().get())
                    : inventoryDAO.getAll(filter, filterViewModel.getPage().get(), filterViewModel.getPageSize().get());
        }).thenAccept(page -> {
            var inventoryViewModels = page.getContent()
                    .stream()
                    .map(i -> new InventoryViewModel(i.getId(), i.getName(), i.getQuantity(), i.getLocation()))
                    .toList();

            Platform.runLater(() -> {
                filterViewModel.getTotalPages().set(page.getTotalPages());
                inventoryTable.getItems().clear();
                inventoryTable.getItems().addAll(inventoryViewModels);
                fetchingEntities.set(false);
            });
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
    
    public void clearFilter(ActionEvent actionEvent){
        locationDropDown.getSelectionModel().clearSelection();
        filterViewModel.getNameSearch().set(null);
        filterViewModel.getqMin().set(null);
        filterViewModel.getqMax().set(null);
        
    }
}
