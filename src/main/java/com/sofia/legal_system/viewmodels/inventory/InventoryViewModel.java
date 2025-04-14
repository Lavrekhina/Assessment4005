package com.sofia.legal_system.viewmodels.inventory;

import javafx.beans.property.*;

public class InventoryViewModel {
    private SimpleObjectProperty<Integer> id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty location;

    public InventoryViewModel() {
        this(null, "", 0, "");
    }

    public InventoryViewModel(Integer id, String name, int quantity, String location) {
        this.id = new SimpleObjectProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.location = new SimpleStringProperty(location);

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
    public String getName() {
        return name.get();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity.get();
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location.set(location);
    }


    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public StringProperty locationProperty() {
        return location;
    }
}
