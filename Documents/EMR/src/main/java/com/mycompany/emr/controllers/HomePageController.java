/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.emr.controllers;

import com.mycompany.emr.App;
import com.mycompany.emr.models.UserModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author mgmoh
 */
public class HomePageController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private final UserModel userModel = new UserModel();

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Username and password are required.");
            return;
        }

        if (userModel.validateCredentials(username, password)) {
            int roleId = userModel.getUserRole(username);
            navigateToDashboard(roleId);
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void navigateToDashboard(int roleId) throws IOException {
        String sceneName;
        switch (roleId) {
            case 1:
                sceneName = "admin-dashboard";
                break;
            case 2:
                sceneName = "registrar-dashboard";
                break;
            case 3:
                sceneName = "nurse-dashboard";
                break;
            case 4:
                sceneName = "doctor-dashboard";
                break;
            default: {
                showAlert(AlertType.ERROR, "Access Denied", "Role not recognized.");
                return;
            }
        }
        if (sceneName != null) {
            SceneController.switchScene(sceneName);
        }

    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        System.out.println("HomePageController initialized");
    }

    @FXML
    private void handleSignUp() throws IOException {
        SceneController.switchScene("sign-up");
        System.out.println("Sign-Up button clicked");
    }

//    @FXML
//    private void handleLogin() {
//        // Logic for handling login
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//        System.out.println("Login attempted with username: " + username + " and password: " + password);
//    }
}
