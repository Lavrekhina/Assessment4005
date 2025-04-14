package com.sofia.legal_system.viewmodels.shipments;

import javafx.beans.property.*;

public class ShipmentViewModel {
    private SimpleObjectProperty<Integer> id;
    private SimpleStringProperty date;
    private SimpleStringProperty destination;
    private SimpleStringProperty shipmentStatus;

    public ShipmentViewModel() {
        this(null, "", "", "");
    }

    public ShipmentViewModel(Integer id, String destination, String date, String shipmentStatus) {
        this.id = new SimpleObjectProperty(id);
        this.date = new SimpleStringProperty(date);
        this.destination = new SimpleStringProperty(destination);
        this.shipmentStatus = new SimpleStringProperty(shipmentStatus);

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
    public String getDestination() {
        return destination.get();
    }

    /**
     * @param destination the quantity to set
     */
    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    /**
     * @return the status
     */
    public String getShipmentStatus() {
        return shipmentStatus.get();
    }

    /**
     * @param status the status to set
     */
    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus.set(shipmentStatus);
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
