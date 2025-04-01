/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.service.impls;

import com.sofia.legal_system.Legal_system;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

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

            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
