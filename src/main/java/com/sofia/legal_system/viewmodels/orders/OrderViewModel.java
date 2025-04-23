package com.sofia.legal_system.viewmodels.orders;

import javafx.beans.property.*;

public class OrderViewModel {
    private final SimpleObjectProperty<Integer> id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty customerName;
    private final SimpleStringProperty status;

    public OrderViewModel() {
        this(null, "", "", "");
    }

    public OrderViewModel(Integer id, String date, String customerName, String status) {
        this.id = new SimpleObjectProperty<>(id);
        this.date = new SimpleStringProperty(date);
        this.customerName = new SimpleStringProperty(customerName);
        this.status = new SimpleStringProperty(status);

    }

    public Integer getId() {
        return id.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public String getStatus() {
        return status.get();
    }


    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
