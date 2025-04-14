package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.shipments.ShipmentViewModel;
import com.sofia.legal_system.viewmodels.shipments.ShipmentsFilterViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;

public class ShipmentsController extends BasePagingController {

    public Button addBtn;
    public Button deleteBtn;
    public TableView<ShipmentViewModel> shipmentsTable;

    private final ShipmentDAO shipmentDAO = new ShipmentDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public ComboBox destinationDropDown;
    public DatePicker dateMinField;
    public DatePicker dateMaxField;
    public ComboBox shipmentStatusDropDown;
    private final ShipmentsFilterViewModel filterViewModel = new ShipmentsFilterViewModel();
    public ComboBox<Integer> pageSizeDropDown;
    public Pagination pagination;

    @FXML
    public void initialize() {

        TableColumn<ShipmentViewModel, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<ShipmentViewModel, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        TableColumn<ShipmentViewModel, String> shipmentStatusColumn = new TableColumn<>("Shipment Status");
        shipmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentStatus"));
        TableColumn<ShipmentViewModel, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        shipmentsTable.getColumns().add(idColumn);
        shipmentsTable.getColumns().add(dateColumn);
        shipmentsTable.getColumns().add(destinationColumn);
        shipmentsTable.getColumns().add(shipmentStatusColumn);

        editBtn.disableProperty().bind(shipmentsTable.getSelectionModel().selectedItemProperty().isNull());
        deleteBtn.disableProperty().bind(shipmentsTable.getSelectionModel().selectedItemProperty().isNull());
        loadingProgressBar.visibleProperty().bind(fetchingEntities);

        dateMinField.valueProperty().bindBidirectional(filterViewModel.getqMin());
        dateMaxField.valueProperty().bindBidirectional(filterViewModel.getqMax());
        shipmentStatusDropDown.setItems(FXCollections.observableArrayList("Delivered", "Dispached", "In transit", "Canceled"));
        shipmentStatusDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            filterViewModel.getstatusSearch().setValue(newValue==null?null:newValue.toString());
        });
        destinationDropDown.setItems(FXCollections.observableArrayList("London", "Manchester"));
        destinationDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            filterViewModel.getDestinationSearch().setValue(newValue==null?null:newValue.toString());
        });

        initPaging(pagination, pageSizeDropDown, filterViewModel, p -> {
            refreshTable(null);
        });

        refreshTable(null);
    }

    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            String filter = filterViewModel.toSqlFilter();
            return (filter.isEmpty()
                    ? shipmentDAO.getAll(filterViewModel.getPage().get(), filterViewModel.getPageSize().get())
                    : shipmentDAO.getAll(filter, filterViewModel.getPage().get(), filterViewModel.getPageSize().get()));
        }).thenAccept(page -> {
            var ordersViewModels = page.getContent().stream().map(i -> new ShipmentViewModel(i.getId(), i.getDestination(), i.getDate(), i.getShipmentStatus())).toList();
            Platform.runLater(() -> {
                shipmentsTable.getItems().clear();
                shipmentsTable.getItems().addAll(ordersViewModels);
                fetchingEntities.set(false);
                filterViewModel.getTotalPages().set(page.getTotalPages());
            });

        });
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml");
    }

    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml", shipmentsTable.getSelectionModel().getSelectedItem());
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
            shipmentsTable.getSelectionModel().getSelectedItems().stream().map(ShipmentViewModel::getId).forEach(integer -> {
                try {
                    shipmentDAO.deleteById(integer);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            return true;
        }).thenAccept(ordersViewModels -> {
            refreshTable(null);
            fetchingEntities.set(false);
        });

    }

    public void filterEntities(ActionEvent actionEvent) {
        refreshTable(null);
    }
    public void clearFilter(ActionEvent actionEvent){
        shipmentStatusDropDown.getSelectionModel().clearSelection();
        destinationDropDown.getSelectionModel().clearSelection();
        filterViewModel.getqMin().set(null);
        filterViewModel.getqMax().set(null);
    }
}
