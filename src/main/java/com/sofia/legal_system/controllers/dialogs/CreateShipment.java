/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.model.Shipment;
import com.sofia.legal_system.service.impls.GUIService;
import com.sofia.legal_system.viewmodels.shipments.ShipmentViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import utils.Consts;
import utils.StringDateConverter;
import utils.ValidationUtils;

public class CreateShipment extends BaseDataDrivenController<ShipmentViewModel> {
    public DatePicker shipmentDatePicker;
    public Button cancelBtn;
    public Button saveBtn;
    public Label shipmentDateFieldError;
    public Label destinationFieldError;
    public ComboBox<String> destinationDropDown;
    public Label statusFieldError;
    public ComboBox<String> statusDropDown;
    private ShipmentViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final ShipmentDAO shipmentDAO = new ShipmentDAO();

    @Override
    public void init() {
        viewModel = data != null ? data : new ShipmentViewModel();

        viewModel.dateProperty().addListener((observableValue, number, t1) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validDate(viewModel.getDate());
            shipmentDateFieldError.setText(valid ? "" : "Date field is required and should be formated like 'yyyy-MM-dd'");
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

        statusDropDown.setItems(FXCollections.observableArrayList(Consts.SHIPMENT_STATUSES));
        viewModel.shipmentStatusProperty().bindBidirectional(statusDropDown.valueProperty());

        destinationDropDown.setItems(FXCollections.observableArrayList(Consts.LOCATIONS));
        viewModel.destinationProperty().bindBidirectional(destinationDropDown.valueProperty());

        viewModel.dateProperty().bindBidirectional(shipmentDatePicker.valueProperty(), new StringDateConverter());

        saveBtn.disableProperty().bind(isValid.not());
        statusDropDown.getSelectionModel().selectFirst();
        destinationDropDown.getSelectionModel().selectFirst();

        isFullyValid();
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

            GUIService.showConfirmationAlert("Shipment", "Shipment with date %s successfully saved!".formatted(shipment.getDate()));
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            GUIService.showErrorAlert("Shipment saving Error", e.getMessage());
        }

    }

    private void isFullyValid() {
        Boolean valid = ValidationUtils.validDate(viewModel.getDate())
                && ValidationUtils.validRequiredString(viewModel.getDestination())
                && ValidationUtils.validRequiredString(viewModel.getShipmentStatus());

        isValid.setValue(valid);
    }

}

