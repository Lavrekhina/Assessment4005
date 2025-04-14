/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.model.Inventory;
import com.sofia.legal_system.viewmodels.inventory.InventoryViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import utils.ValidationUtils;

public class CreateInventory extends BaseDataDrivenController<InventoryViewModel> {

    public Button cancelBtn;
    public Button saveBtn;
    public Label locationFieldError;
    public Label quantityFieldError;
    public TextField quantityField;
    public Label nameFieldError;
    public TextField nameField;
    public ComboBox locationDropDown;
    private InventoryViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @FXML
    public void initialize() {
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void save(ActionEvent actionEvent) {
        try {
            Inventory inventory = new Inventory(viewModel.getId(), viewModel.getName(), viewModel.getQuantity(), viewModel.getLocation());
            if (viewModel.getId() != null) {
                inventoryDAO.update(inventory);
            } else {
                inventoryDAO.insert(inventory);
            }

            Stage stage = (Stage) saveBtn.getScene().getWindow();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Inventory");
            alert.setHeaderText(null);
            alert.setContentText("Item with name %s successfully added!".formatted(inventory.getName()));

            alert.showAndWait();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void isFullyValid() {
        Boolean valid = ValidationUtils.validRequiredString(viewModel.getName())
                && locationDropDown.getSelectionModel().getSelectedItem() != null
                && ValidationUtils.validInteger(viewModel.getQuantity(), 0);

        isValid.setValue(valid);
    }

    @Override
    public void init() {
        viewModel = data != null ? data : new InventoryViewModel();

        quantityField.textProperty().bindBidirectional(viewModel.quantityProperty(), new NumberStringConverter());
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());

        viewModel.quantityProperty().addListener((observableValue, number, t1) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validInteger(viewModel.getQuantity(), 0);
            quantityFieldError.setText(valid ? "" : "Quantity field is required and should not be less than zero");
        });

        viewModel.nameProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getName());
            nameFieldError.setText(valid ? "" : "Name field is required");
        });

        locationDropDown.setItems(FXCollections.observableArrayList("London", "Manchester"));
        locationDropDown.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setLocation(locationDropDown.getSelectionModel().getSelectedItem().toString());
        });
        saveBtn.disableProperty().bind(isValid.not());
        locationDropDown.getSelectionModel().selectFirst();
        isFullyValid();
    }

}
