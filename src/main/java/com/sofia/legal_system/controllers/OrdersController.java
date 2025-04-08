package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.OrderDAO;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.InventoryViewModel;
import com.sofia.legal_system.viewmodels.OrderViewModel;
import com.sofia.legal_system.viewmodels.OrdersFilterViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;

public class OrdersController {
    public Button addBtn;
    public Button deleteBtn;
    public TableView<OrderViewModel> ordersTable;

    private final OrderDAO ordersDAO = new OrderDAO();
    private final SimpleBooleanProperty fetchingEntities = new SimpleBooleanProperty(false);
    public Button refreshBtn;
    public Button editBtn;
    public ProgressBar loadingProgressBar;
    public TextField customerNameSearchTextField;
    public DatePicker dateMinField;
    public DatePicker dateMaxField;
    public ComboBox statusDropDown;
    private final OrdersFilterViewModel filterViewModel = new OrdersFilterViewModel();

    @FXML
    public void initialize() {

        TableColumn<OrderViewModel, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<OrderViewModel, String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        TableColumn<OrderViewModel, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<OrderViewModel, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        ordersTable.getColumns().add(idColumn);
        ordersTable.getColumns().add(dateColumn);
        ordersTable.getColumns().add(customerNameColumn);
        ordersTable.getColumns().add(statusColumn);


        editBtn.disableProperty().bind(ordersTable.getSelectionModel().selectedItemProperty().isNull());
        deleteBtn.disableProperty().bind(ordersTable.getSelectionModel().selectedItemProperty().isNull());
        loadingProgressBar.visibleProperty().bind(fetchingEntities);
        customerNameSearchTextField.textProperty().bindBidirectional(filterViewModel.getcustomerNameSearch());
        dateMinField.valueProperty().bindBidirectional(filterViewModel.getqMin());
        dateMaxField.valueProperty().bindBidirectional(filterViewModel.getqMax());
        statusDropDown.setItems(FXCollections.observableArrayList("Created", "Prepared","Ready for shipment"));
        statusDropDown.getSelectionModel().selectedItemProperty().addListener((obs,oldValue, newValue)-> {
            filterViewModel.getstatusSearch().setValue(newValue.toString());
            
       
        });
    }

    public void refreshTable(ActionEvent actionEvent) {
        fetchingEntities.set(true);
        CompletableFuture.supplyAsync(() -> {
            try {
                String filter = filterViewModel.toSqlFilter();  
                return (filter.isEmpty()? ordersDAO.getAll():ordersDAO.getAll(filter)).stream().map(i -> new OrderViewModel(i.getId(), i.getCustomerName(), i.getDate(), i.getStatus())).toList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(ordersViewModels -> {
            ordersTable.getItems().clear();
            ordersTable.getItems().addAll(ordersViewModels);
            fetchingEntities.set(false);
        });
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-order.fxml");
    }
    public void editRow(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-order.fxml",  ordersTable.getSelectionModel().getSelectedItem());
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
            ordersTable.getSelectionModel().getSelectedItems().stream().map(OrderViewModel::getId).forEach(integer -> {
                try {
                    ordersDAO.deleteById(integer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
