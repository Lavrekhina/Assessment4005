package com.sofia.legal_system.controllers;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.model.Inventory;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.InventoryViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {
    public Button addBtn;
    public Button deleteBtn;
    public TableView<InventoryViewModel> inventoryTable;

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @FXML
    public void initialize() throws SQLException {
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

    }

    public void refreshTable(ActionEvent actionEvent) throws SQLException {
        inventoryTable.getItems().clear();
        inventoryTable.getItems().addAll(inventoryDAO.getAll().stream().map(i -> {
            return new InventoryViewModel(i.getId(), i.getName(), i.getQuantity(), i.getLocation());
        }).toList());
    }

    public void openCreateDialog(ActionEvent actionEvent) {
        GUIService.showDialog("/dialogs/create-inventory.fxml");
    }
}
