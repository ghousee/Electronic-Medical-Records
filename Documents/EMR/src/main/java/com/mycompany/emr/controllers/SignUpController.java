/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 *
 * @author mgmoh
 */
public class SignUpController {
//
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private PasswordField passwordField;
//
//    @FXML
//    private ComboBox<String> roleComboBox;
//
//    public void initialize() {
//        System.out.println("SignUpController initialized");
//
//        // Populate ComboBox with user-friendly role names
//        roleComboBox.getItems().addAll("Admin", "Registration Officer", "Nurse", "Doctor");
//    }
//
//    @FXML
//    private void registerUser() {
//        String username = usernameField.getText().trim();
//        String password = passwordField.getText().trim();
//        String role = roleComboBox.getValue();
//
//        // Validate inputs
//        if (username.isEmpty() || password.isEmpty() || role == null) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
//            return;
//        }
//
//        if (password.length() < 6) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 6 characters long.");
//            return;
//        }
//
//        int roleId = getRoleId(role);
//
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            if (connection == null) {
//                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database. Please try again later.");
//                return;
//            }
//
//            System.out.println("Debug: Username = " + username + ", Role ID = " + roleId + ", Password = " + password);
//
//            String query = "INSERT INTO Users (username, password, role_id, created_at) VALUES (?, ?, ?, NOW())";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, username);
//            statement.setString(2, password);
//            statement.setInt(3, roleId);
//
//            int rowsInserted = statement.executeUpdate();
//
//            if (rowsInserted > 0) {
//                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
//                clearFields();
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user. Please try again.");
//            }
//        } catch (SQLException e) {
//            handleSQLException(e);
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private int getRoleId(String role) {
//        switch (role) {
//            case "Admin":
//                return 1;
//            case "Registrar":
//                return 2;
//            case "Nurse":
//                return 3;
//            case "Doctor":
//                return 4;
//            default:
//                throw new IllegalArgumentException("Unknown role: " + role);
//        }
//    }
//
//    private void handleSQLException(SQLException e) {
//        if (e.getMessage().contains("Duplicate entry")) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different username.");
//        } else if (e.getMessage().contains("foreign key constraint fails")) {
//            showAlert(Alert.AlertType.ERROR, "Database Error", "Invalid role selected. Please contact support.");
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Database Error", "An unexpected database error occurred: " + e.getMessage());
//        }
//        e.printStackTrace();
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void clearFields() {
//        usernameField.clear();
//        passwordField.clear();
//        roleComboBox.setValue(null);
//    }

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    private final UserModel userModel = new UserModel(); // Communicates with the Model

    @FXML
    public void initialize() {
        // Populate role combo box
        roleComboBox.getItems().addAll("Admin", "Registration Officer", "Nurse", "Doctor");
    }

    @FXML
    public void loginRedirect() throws IOException {
        SceneController.switchScene("homepage");
        System.out.println("Login redirect button clicked");
    }

    @FXML
    public void handleRegister() {
        // Get user input
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        int roleId = getRoleId(role);

        // Delegate business logic to UserModel
        if (userModel.checkUserExists(username)) {
            showAlert(AlertType.ERROR, "Registration Error", "Username already exists.");
            return;
        }

        if (userModel.registerUser(username, password, roleId)) {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");
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
