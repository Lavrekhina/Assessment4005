package com.prins.legal_system.model;

public class Inventory {
    private Integer id;
    private String name;
    private int quantity;
    private String location;

    public Inventory() {
    }

    public Inventory(Integer id, String name, int quantity, String location) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }

    public Inventory(String name, int quantity, String location) {
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
