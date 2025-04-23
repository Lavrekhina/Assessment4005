/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.service.impls;

import com.sofia.legal_system.Legal_system;
import com.sofia.legal_system.controllers.dialogs.BaseDataDrivenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class GUIService {
    private static Stage primaryStage;

    public static void init(Stage stage) {
        if (primaryStage != null) {
            return;
        }

        primaryStage = stage;
    }

    public static void showDialog(String name) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = Legal_system.class.getResource(name);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();

            if (loader.getController() instanceof BaseDataDrivenController controller) {
                controller.init();
            }

            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showDialog(String name, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = Legal_system.class.getResource(name);
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            if (loader.getController() instanceof BaseDataDrivenController controller) {
                controller.setData(data);
                controller.init();
            }
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Optional<ButtonType> showConfirmationAlert(String title, String message) {
        return showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    public static Optional<ButtonType> showErrorAlert(String title, String message) {
        return showAlert(Alert.AlertType.ERROR, title, message);
    }

    private static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait();
    }
}
