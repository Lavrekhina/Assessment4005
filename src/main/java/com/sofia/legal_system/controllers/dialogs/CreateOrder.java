/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.OrderDAO;
import com.sofia.legal_system.model.Order;
import com.sofia.legal_system.viewmodels.orders.OrderViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import utils.ValidationUtils;

public class CreateOrder extends BaseDataDrivenController<OrderViewModel> {
    public DatePicker orderDatePicker;
    public Button cancelBtn;
    public Button saveBtn;
    public Label orderDateFieldError;
    public Label customerNameFieldError;
    public TextField customerNameField;
    public Label statusFieldError;
    public ComboBox statusDropDown;
    private OrderViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void save(ActionEvent actionEvent) {
        try {
            Order order = new Order(viewModel.getId(), viewModel.getDate(), viewModel.getCustomerName(), viewModel.getStatus());
            if (viewModel.getId() != null) {
                orderDAO.update(order);
            } else {
                orderDAO.insert(order);
            }

            Stage stage = (Stage) saveBtn.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order");
            alert.setHeaderText(null);
            alert.setContentText("Order for customer with name %s successfully added!".formatted(order.getCustomerName()));

            alert.showAndWait();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void isFullyValid() {
        Boolean valid = ValidationUtils.validDate(viewModel.getDate())
                && ValidationUtils.validRequiredString(viewModel.getCustomerName())
                && ValidationUtils.validRequiredString(viewModel.getStatus());

        isValid.setValue(valid);
    }

    @Override
    public void init() {
        viewModel = data != null ? data : new OrderViewModel();

        customerNameField.textProperty().bindBidirectional(viewModel.customerNameProperty());

        viewModel.dateProperty().addListener((observableValue, number, t1) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validDate(viewModel.getDate());
            orderDateFieldError.setText(valid ? "" : "Date field is required and should be formated like 'YYYY-MM-dd'");
        });

        viewModel.customerNameProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getCustomerName());
            customerNameFieldError.setText(valid ? "" : "Customer name field is required");
        });

        viewModel.statusProperty().addListener((observableValue, oldValue, newValue) -> {
            isFullyValid();
            boolean valid = ValidationUtils.validRequiredString(viewModel.getStatus());
            statusFieldError.setText(valid ? "" : "Status field is required");

        });

        statusDropDown.setItems(FXCollections.observableArrayList("Created", "Prepared", "Ready for shipment"));
        statusDropDown.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setStatus(statusDropDown.getSelectionModel().getSelectedItem().toString());
        });

        orderDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            viewModel.setDate(newValue.toString());
        });

        saveBtn.disableProperty().bind(isValid.not());
        statusDropDown.getSelectionModel().selectFirst();
        isFullyValid();
    }
}

