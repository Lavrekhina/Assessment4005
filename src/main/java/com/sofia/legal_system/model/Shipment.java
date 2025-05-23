/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.model;

import utils.StringUtils;

/**
 *
 * @author soflavre
 */
public class Shipment {
    private Integer id;
    private String destination;
    private String date;
    private String shipmentStatus;

    public Shipment(){
    }
    
    public Shipment(Integer id, String destination, String date, String deliveryStatus){
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.shipmentStatus = deliveryStatus;
    }
    
    public Shipment(String destination, String date, String deliveryStatus){
        this(null, destination, date, deliveryStatus);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the deliveryStatus
     */
    public String getShipmentStatus() {
        return shipmentStatus;
    }

    /**
     * @param deliveryStatus the deliveryStatus to set
     */
    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s Destination: %s Date: %s Shipment Status: %s",
               StringUtils.fillTo(String.valueOf(id), 15), 
               StringUtils.fillTo(destination, 15), 
               StringUtils.fillTo(date, 15), 
               StringUtils.fillTo(shipmentStatus, 15));
    }
}
