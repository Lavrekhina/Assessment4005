package com.sofia.legal_system.viewmodels.inventory;

import javafx.beans.property.*;

public class InventoryViewModel {
    private final SimpleObjectProperty<Integer> id;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty location;

    public InventoryViewModel() {
        this(null, "", 0, "");
    }

    public InventoryViewModel(Integer id, String name, int quantity, String location) {
        this.id = new SimpleObjectProperty<>(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.location = new SimpleStringProperty(location);
    }

    public Integer getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public String getLocation() {
        return location.get();
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
