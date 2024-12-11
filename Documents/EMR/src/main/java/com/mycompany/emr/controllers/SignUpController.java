/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ghouse
 */

package com.mycompany.emr.controllers;

import com.mycompany.emr.App;
import com.mycompany.emr.models.UserModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.DatabaseConnection;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    private final UserModel userModel = new UserModel(); 

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Registration Officer", "Nurse", "Doctor");
    }

    @FXML
    public void loginRedirect() throws IOException {
        SceneController.switchScene("homepage");
        System.out.println("Login redirect button clicked");
    }

    @FXML
    public void handleRegister() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        int roleId = getRoleId(role);

        if (userModel.checkUserExists(username)) {
            showAlert(AlertType.ERROR, "Registration Error", "Username already exists.");
            return;
        }

        if (userModel.registerUser(username, password, roleId)) {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");
            SceneController.switchScene("homepage");
        } else {
            showAlert(AlertType.ERROR, "Registration Error", "Failed to register user.");
        }
    }

    private int getRoleId(String role) {
        switch (role) {
            case "Admin":
                return 1;
            case "Registration Officer":
                return 2;
            case "Nurse":
                return 3;
            case "Doctor":
                return 4;
            default:
                return 0;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
