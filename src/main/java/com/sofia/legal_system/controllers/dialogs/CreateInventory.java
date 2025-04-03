/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.model.Inventory;
import com.sofia.legal_system.viewmodels.InventoryViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;


import java.sql.SQLException;

import utils.ValidationUtils;

public class CreateInventory extends BaseDataDrivenController<InventoryViewModel> {

    public Button cancelBtn;
    public Button saveBtn;
    public TextField locationField;
    public Label locationFieldError;
    public Label quantityFieldError;
    public TextField quantityField;
    public Label nameFieldError;
    public TextField nameField;
    private InventoryViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @FXML
    public void initialize() {
    }

    public void cancel(ActionEvent actionEvent) {
    }

    public void save(ActionEvent actionEvent) {
        try {
            Inventory inventory = new Inventory(viewModel.getId(), viewModel.getName(), viewModel.getQuantity(), viewModel.getLocation());
            if (viewModel.getId() != null) {
                inventoryDAO.update(inventory);
            } else {
                inventoryDAO.insert(inventory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void isFullyValid() {
        Boolean valid = ValidationUtils.validRequiredString(viewModel.getName())
                && ValidationUtils.validRequiredString(viewModel.getLocation())
                && ValidationUtils.validInteger(viewModel.getQuantity(), 0);

        isValid.setValue(valid);
    }

    @Override
    public void init() {
        viewModel = data != null ? data : new InventoryViewModel();

        locationField.textProperty().bindBidirectional(viewModel.locationProperty());
        quantityField.textProperty().bindBidirectional(viewModel.quantityProperty(), new NumberStringConverter());
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());

        viewModel.quantityProperty().addListener((observableValue, number, t1) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validInteger(viewModel.getQuantity(), 0);
            quantityFieldError.setText(valid ? "" : "Quantity field is required and should not be less than zero");
        });

        viewModel.locationProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getLocation());
            locationFieldError.setText(valid ? "" : "Location field is required");
        });

        viewModel.nameProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getName());
            nameFieldError.setText(valid ? "" : "Name field is required");

        });

        saveBtn.disableProperty().bind(isValid.not());

        isFullyValid();
    }
}

