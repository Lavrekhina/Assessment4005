package com.sofia.legal_system.viewmodels.shipments;

import javafx.beans.property.*;

public class ShipmentViewModel {
    private final SimpleObjectProperty<Integer> id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty destination;
    private final SimpleStringProperty shipmentStatus;

    public ShipmentViewModel() {
        this(null, "", "", "");
    }

    public ShipmentViewModel(Integer id, String destination, String date, String shipmentStatus) {
        this.id = new SimpleObjectProperty<>(id);
        this.date = new SimpleStringProperty(date);
        this.destination = new SimpleStringProperty(destination);
        this.shipmentStatus = new SimpleStringProperty(shipmentStatus);
    }

    public Integer getId() {
        return id.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getDestination() {
        return destination.get();
    }
    public String getShipmentStatus() {
        return shipmentStatus.get();
    }

    public StringProperty dateProperty() {
        return date;
    }
    public StringProperty destinationProperty() {
        return destination;
    }
    public StringProperty shipmentStatusProperty() {
        return shipmentStatus;
    }
}
