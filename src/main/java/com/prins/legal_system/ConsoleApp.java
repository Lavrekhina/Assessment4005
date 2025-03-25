/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prins.legal_system;
import com.prins.legal_system.DAO.InventoryDAO;
import com.prins.legal_system.model.Inventory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author soflavre
 */
public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryDAO inventoryDAO = new InventoryDAO();
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
//                case 2: processOrders(); break;
//                case 3: trackShipments(); break;
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

