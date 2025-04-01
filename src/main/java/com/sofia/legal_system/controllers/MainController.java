package com.sofia.legal_system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainController {
    public ListView<MenuItem> listMenu;
    public AnchorPane inventoryNode;
    public AnchorPane shipmentsNode;
    public AnchorPane ordersNode;
    public StackPane viewsStack;

    @FXML
    public void initialize() {
        listMenu.getItems().addAll(
                new MenuItem("Inventory", inventoryNode),
                new MenuItem("Orders", ordersNode),
                new MenuItem("Shipments", shipmentsNode));

        listMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if(newValue == null) {
                return;
            }
            newValue.pane.toFront();
        });
    }


    private static class MenuItem {
        private String label;
        private Pane pane;

        public MenuItem(String label, Pane pane) {
            this.label = label;
            this.pane = pane;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
