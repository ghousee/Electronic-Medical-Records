/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.controllers;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/com/mycompany/emr/" + fxmlFile + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            System.out.println("Scene switched to: " + fxmlFile);
        } catch (IOException e) {
            System.err.println("Failed to load FXML File: " + fxmlFile);
            e.printStackTrace();
        }
    }

}
