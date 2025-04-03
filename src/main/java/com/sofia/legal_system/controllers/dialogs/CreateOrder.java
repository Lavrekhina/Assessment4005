/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.controllers.dialogs;

import com.sofia.legal_system.DAO.OrderDAO;
import com.sofia.legal_system.model.Order;
import com.sofia.legal_system.viewmodels.OrderViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;


import java.sql.SQLException;

import utils.ValidationUtils;

public class CreateOrder extends BaseDataDrivenController<OrderViewModel> {

    public Button cancelBtn;
    public Button saveBtn;
    public TextField orderDateField;
    public Label orderDateFieldError;
    public Label customerNameFieldError;
    public TextField customerNameField;
    public Label statusFieldError;
    public TextField statusField;
    private OrderViewModel viewModel;
    private final SimpleBooleanProperty isValid = new SimpleBooleanProperty(false);
    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    public void initialize() {
    }

    public void cancel(ActionEvent actionEvent) {
    }

    public void save(ActionEvent actionEvent) {
        try {
            Order order = new Order(viewModel.getId(), viewModel.getDate(), viewModel.getCustomerName(), viewModel.getStatus());
            if (viewModel.getId() != null) {
                orderDAO.update(order);
            } else {
                orderDAO.insert(order);
            }
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

        orderDateField.textProperty().bindBidirectional(viewModel.dateProperty());
        customerNameField.textProperty().bindBidirectional(viewModel.customerNameProperty());
        statusField.textProperty().bindBidirectional(viewModel.statusProperty());

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

        saveBtn.disableProperty().bind(isValid.not());

        isFullyValid();
    }
}

