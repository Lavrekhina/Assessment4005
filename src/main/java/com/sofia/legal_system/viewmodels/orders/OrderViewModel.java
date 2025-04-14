package com.sofia.legal_system.viewmodels.orders;

import javafx.beans.property.*;

public class OrderViewModel {
    private SimpleObjectProperty<Integer> id;
    private SimpleStringProperty date;
    private SimpleStringProperty customerName;
    private SimpleStringProperty status;

    public OrderViewModel() {
        this(null, "", "", "");
    }

    public OrderViewModel(Integer id, String date, String customerName, String status) {
        this.id = new SimpleObjectProperty(id);
        this.date = new SimpleStringProperty(date);
        this.customerName = new SimpleStringProperty(customerName);
        this.status = new SimpleStringProperty(status);

    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id.get();
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     * @return the name
     */
    public String getDate() {
        return date.get();
    }

    /**
     * @param date the name to set
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * @return the quantity
     */
    public String getCustomerName() {
        return customerName.get();
    }

    /**
     * @param quantity the quantity to set
     */
    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status.set(status);
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
