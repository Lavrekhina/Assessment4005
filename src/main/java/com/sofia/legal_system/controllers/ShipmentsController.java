package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.OrderDAO;
import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.InventoryViewModel;
import com.sofia.legal_system.viewmodels.OrderViewModel;
import com.sofia.legal_system.viewmodels.OrdersFilterViewModel;
import com.sofia.legal_system.viewmodels.ShipmentViewModel;
import com.sofia.legal_system.viewmodels.ShipmentsFilterViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;

public class ShipmentsController {
    public Button addBtn;
    public Button deleteBtn;
    public TableView<ShipmentViewModel> shipmentsTable;

    private final ShipmentDAO shipmentDAO = new ShipmentDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public ComboBox destinationDropDown ; 
    public DatePicker dateMinField;
    public DatePicker dateMaxField;
    public ComboBox shipmentStatusDropDown;
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
        
        dateMinField.valueProperty().bindBidirectional(filterViewModel.getqMin());
        dateMaxField.valueProperty().bindBidirectional(filterViewModel.getqMax());
        shipmentStatusDropDown.setItems(FXCollections.observableArrayList("Delivered","Dispached", "In transit","Canceled"));
        shipmentStatusDropDown.getSelectionModel().selectedItemProperty().addListener((obs,oldValue, newValue)-> {
            filterViewModel.getstatusSearch().setValue(newValue.toString());
        });
        destinationDropDown.setItems(FXCollections.observableArrayList("London","Manchester"));
        destinationDropDown.getSelectionModel().selectedItemProperty().addListener((obs,oldValue, newValue)-> {
            filterViewModel.getDestinationSearch().setValue(newValue.toString());
        });
    }

    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            try {
                String filter = filterViewModel.toSqlFilter();  
                return (filter.isEmpty()? shipmentDAO.getAll():shipmentDAO.getAll(filter)).stream().map(i -> new ShipmentViewModel(i.getId(), i.getDestination(), i.getDate(), i.getShipmentStatus())).toList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(ordersViewModels -> {
            shipmentsTable.getItems().clear();
            shipmentsTable.getItems().addAll(ordersViewModels);
            fetchingEntities.set(false);
        });
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml");
    }
    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-shipment.fxml",  shipmentsTable.getSelectionModel().getSelectedItem());
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
}
