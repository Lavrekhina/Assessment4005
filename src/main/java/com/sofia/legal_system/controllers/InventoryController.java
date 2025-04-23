package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.model.KeyValuePair;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.inventory.InventoryFilterViewModel;
import com.sofia.legal_system.viewmodels.inventory.InventoryViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.IntegerStringConverter;
import utils.Consts;

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
        locationDropDown.setItems(FXCollections.observableArrayList(Consts.LOCATIONS));
        filterViewModel.getLocationSearch().bind(locationDropDown.getSelectionModel().selectedItemProperty());

        initPagingAndSorting(filterViewModel);
        refreshTable(null);
    }

    @Override
    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            String filter = filterViewModel.toSqlFilter();
            return filter.isEmpty()
                    ? inventoryDAO.getAll(filterViewModel.getPage().get(), filterViewModel.getPageSize().get(), filterViewModel.toSort())
                    : inventoryDAO.getAll(filter, filterViewModel.getPage().get(), filterViewModel.getPageSize().get(), filterViewModel.toSort());
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

    @Override
    protected List<KeyValuePair<String, String>> getProps() {
        return List.of(KeyValuePair.of("ID", "item_id"),
                KeyValuePair.of("Name", "item_name"),
                KeyValuePair.of("Quantity", "item_quantity"),
                KeyValuePair.of("Location", "item_location"));
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-inventory.fxml");
    }

    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-inventory.fxml", inventoryTable.getSelectionModel().getSelectedItem());
    }

    @Override
    public void deleteSelected() {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            inventoryTable.getSelectionModel().getSelectedItems().stream().map(InventoryViewModel::getId).forEach(integer -> {
                try {
                    inventoryDAO.deleteById(integer);
                } catch (SQLException ex) {
                    GUIService.showErrorAlert("Error deleting inventory item", ex.getMessage());
                    throw new RuntimeException(ex);
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

    public void clearFilter(ActionEvent actionEvent) {
        locationDropDown.getSelectionModel().clearSelection();
        filterViewModel.getNameSearch().set(null);
        filterViewModel.getqMin().set(null);
        filterViewModel.getqMax().set(null);

    }
}
