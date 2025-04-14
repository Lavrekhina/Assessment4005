/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.model.Shipment;
import com.sofia.legal_system.viewmodels.shipments.ShipmentViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import utils.ValidationUtils;

public class CreateShipment extends BaseDataDrivenController<ShipmentViewModel> {
    public DatePicker shipmentDatePicker;
    public Button cancelBtn;
    public Button saveBtn;
    public Label shipmentDateFieldError;
    public Label destinationFieldError;
    public ComboBox destinationDropDown;
    public Label statusFieldError;
    public ComboBox statusDropDown;
    private ShipmentViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final ShipmentDAO shipmentDAO = new ShipmentDAO();

    @FXML
    public void initialize() {
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void save(ActionEvent actionEvent) {
        try {
            Shipment shipment = new Shipment(viewModel.getId(), viewModel.getDestination(), viewModel.getDate(), viewModel.getShipmentStatus());
            if (viewModel.getId() != null) {
                shipmentDAO.update(shipment);
            } else {
                shipmentDAO.insert(shipment);
            }

            Stage stage = (Stage) saveBtn.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Shipment");
            alert.setHeaderText(null);
            alert.setContentText("Shipment with date %s successfully added!".formatted(shipment.getDate()));

            alert.showAndWait();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void isFullyValid() {
        Boolean valid = ValidationUtils.validDate(viewModel.getDate())
                && ValidationUtils.validRequiredString(viewModel.getDestination())
                && ValidationUtils.validRequiredString(viewModel.getShipmentStatus());

        isValid.setValue(valid);
    }

    @Override
    public void init() {
        viewModel = data != null ? data : new ShipmentViewModel();


        viewModel.dateProperty().addListener((observableValue, number, t1) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validDate(viewModel.getDate());
            shipmentDateFieldError.setText(valid ? "" : "Date field is required and should be formated like 'YYYY-MM-dd'");
        });

        viewModel.destinationProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getDestination());
            destinationFieldError.setText(valid ? "" : "Destination field is required");
        });

        viewModel.shipmentStatusProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getShipmentStatus());
            statusFieldError.setText(valid ? "" : "Status field is required");

        });

        statusDropDown.setItems(FXCollections.observableArrayList("Delivered","Dispached", "In transit","Canceled"));
        statusDropDown.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setShipmentStatus(statusDropDown.getSelectionModel().getSelectedItem().toString());
        });
        
        destinationDropDown.setItems(FXCollections.observableArrayList("London", "Manchester"));
        destinationDropDown.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setDestination(destinationDropDown.getSelectionModel().getSelectedItem().toString());
        });

        shipmentDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setDate(newValue.toString());
        });

        saveBtn.disableProperty().bind(isValid.not());
        statusDropDown.getSelectionModel().selectFirst();
        destinationDropDown.getSelectionModel().selectFirst();

        isFullyValid();
    }
}

