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
public class Order {
    private Integer id;
    private String date;
    private String customerName;
    private String status;

    public Order(){
    }

    public Order(Integer id, String date, String customerName, String status){
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.status = status;
    }
    
    public Order(String date, String customerName, String status){
        this.date = date;
        this.customerName = customerName;
        this.status = status;
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
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s Order Date: %s Customer Name: %s Order Status: %s",
               StringUtils.fillTo(String.valueOf(id), 15), 
               StringUtils.fillTo(date, 15), 
               StringUtils.fillTo(customerName, 15), 
               StringUtils.fillTo(status, 15));
    }
            
}