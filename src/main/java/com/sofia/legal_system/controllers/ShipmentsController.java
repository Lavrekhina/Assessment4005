package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.model.KeyValuePair;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.shipments.ShipmentViewModel;
import com.sofia.legal_system.viewmodels.shipments.ShipmentsFilterViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;

import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;
import utils.Consts;

public class ShipmentsController extends BasePagingController {

    public Button addBtn;
    public Button deleteBtn;
    public TableView<ShipmentViewModel> shipmentsTable;

    private final ShipmentDAO shipmentDAO = new ShipmentDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public ComboBox<String> destinationDropDown;
    public DatePicker dateMinField;
    public DatePicker dateMaxField;
    public ComboBox<String> shipmentStatusDropDown;
    private final ShipmentsFilterViewModel filterViewModel = new ShipmentsFilterViewModel();

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

        dateMinField.valueProperty().bindBidirectional(filterViewModel.getDMin());
        dateMaxField.valueProperty().bindBidirectional(filterViewModel.getDMax());
        shipmentStatusDropDown.setItems(FXCollections.observableArrayList(Consts.SHIPMENT_STATUSES));
        shipmentStatusDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            filterViewModel.getStatusSearch().setValue(newValue);
        });
        destinationDropDown.setItems(FXCollections.observableArrayList(Consts.LOCATIONS));
        destinationDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            filterViewModel.getDestinationSearch().setValue(newValue);
        });

        initPagingAndSorting(filterViewModel);
        refreshTable(null);
    }

    @Override
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

    @Override
    protected List<KeyValuePair<String, String>> getProps() {
        return List.of(KeyValuePair.of("ID", "shipment_id"),
                KeyValuePair.of("Destination", "destination"),
                KeyValuePair.of("Shipment Date", "shipment_date"),
                KeyValuePair.of("Shipment status", "shipment_status"));
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml");
    }

    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml", shipmentsTable.getSelectionModel().getSelectedItem());
    }

    @Override
    public void deleteSelected() {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            shipmentsTable.getSelectionModel().getSelectedItems().stream().map(ShipmentViewModel::getId).forEach(integer -> {
                try {
                    shipmentDAO.deleteById(integer);
                } catch (SQLException ex) {
                    GUIService.showErrorAlert("Error deleting shipment", ex.getMessage());
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

    public void clearFilter(ActionEvent actionEvent) {
        shipmentStatusDropDown.getSelectionModel().clearSelection();
        destinationDropDown.getSelectionModel().clearSelection();
        filterViewModel.getDMin().set(null);
        filterViewModel.getDMax().set(null);
    }
}
