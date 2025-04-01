/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system;
import com.sofia.legal_system.DAO.InventoryDAO;
import com.sofia.legal_system.DAO.OrderDAO;
import com.sofia.legal_system.DAO.ShipmentDAO;
import com.sofia.legal_system.model.Inventory;
import com.sofia.legal_system.model.Order;
import com.sofia.legal_system.model.Shipment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author soflavre
 */
public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
    private static final InventoryDAO inventoryDAO = new InventoryDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final ShipmentDAO shipmentDAO = new ShipmentDAO();
    private static final String DATE_FORMAT = "YYYY-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static void main(final String[] args) throws SQLException {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Manage Inventory");
            System.out.println("2. Process Orders");
            System.out.println("3. Track Shipments");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    manageInventory();
                    break;
                case 2: processOrders(); 
                    break;
                case 3: trackShipments(); 
                    break;
                case 4:
                    System.out.println("Exiting application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageInventory() throws SQLException {
        while (true) {
            System.out.println("\nManage Inventory:");
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Delete Item");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String name = scanner.next();
                    System.out.print("Enter quantity: ");
                    int quantity = getIntInput();
                    System.out.print("Enter item location: ");
                    String location = scanner.next();
                    inventoryDAO.insert(new Inventory(name, quantity, location));
                    System.out.println("Item added successfully.");
                    break;
                case 2:
                    System.out.println("Current Inventory:");
                    showFullInventory();
                    break;
                case 3:
                    System.out.println("Current Inventory:");
                    showFullInventory();
                    System.out.print("Enter Item ID: ");
                    int id = getIntInput();
                    inventoryDAO.deleteById(id);
                    System.out.println("Item deleted successfully.");
                    showFullInventory();
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }

    }
    private static void showFullInventory() throws SQLException {
        var all = inventoryDAO.getAll();
        if (all.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            inventoryDAO.getAll().forEach(inventory -> {
                System.out.println(inventory.toString());
            });
        }

    }
    
    private static void trackShipments() throws SQLException {
    while (true) {
            System.out.println("\nManage Shipments:");
            System.out.println("1. Add Shipment");
            System.out.println("2. View Shipment");
            System.out.println("3. Delete Shipment");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.print("Enter shipment destination: ");
                    String destination = scanner.next();
                    System.out.print("Enter shipment date: ");
                    String date = getDateString();
                    System.out.print("Enter delivery status: ");
                    String deliveryStatus = scanner.next();
                    shipmentDAO.insert(new Shipment(destination, date, deliveryStatus));
                    System.out.println("Shipment added successfully.");
                    break;
                case 2:
                    System.out.println("Current Shipments:");
                    showFullShipments();
                    break;
                case 3:
                    System.out.println("Current Shipments:");
                    showFullShipments();
                    System.out.print("Enter Shipment ID: ");
                    int id = getIntInput();
                    orderDAO.deleteById(id);
                    System.out.println("Shipment deleted successfully.");
                    showFullShipments();
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        } 
    }
    
    private static void showFullShipments() throws SQLException {
        var all = shipmentDAO.getAll();
        if (all.isEmpty()) {
            System.out.println("Shipment is empty.");
        } else {
            shipmentDAO.getAll().forEach(order -> {
                System.out.println(order.toString());
            });
        }
    }
    
    
    
    private static void processOrders() throws SQLException {
       while (true) {
            System.out.println("\nManage Orders:");
            System.out.println("1. Add Order");
            System.out.println("2. View Order");
            System.out.println("3. Delete Order");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    System.out.print("Enter order date: ");
                    String date = getDateString();
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.next();
                    System.out.print("Enter order status: ");
                    String status = scanner.next();
                    orderDAO.insert(new Order(date, customerName, status));
                    System.out.println("Order added successfully.");
                    break;
                case 2:
                    System.out.println("Current Orders:");
                    showFullOrders();
                    break;
                case 3:
                    System.out.println("Current Orders:");
                    showFullOrders();
                    System.out.print("Enter Order ID: ");
                    int id = getIntInput();
                    orderDAO.deleteById(id);
                    System.out.println("Order deleted successfully.");
                    showFullOrders();
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        } 
    }
    private static void showFullOrders() throws SQLException {
        var all = orderDAO.getAll();
        if (all.isEmpty()) {
            System.out.println("Order is empty.");
        } else {
            orderDAO.getAll().forEach(order -> {
                System.out.println(order.toString());
            });
        }
    }
    
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    private static String getDateString() {
        boolean validDate = false;
        String date = null;
        while (!validDate) {
            date = scanner.next();
            try {
                dateFormat.parse(date);
                validDate = true;
            } catch (Exception e) {
                System.out.println("Invalid date. Please try again.");
            }
        }

        return date;
    }
}

