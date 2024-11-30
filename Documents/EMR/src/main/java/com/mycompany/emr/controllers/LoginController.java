///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.emr.controllers;
//
//import com.mycompany.emr.models.UserModel;
//import java.io.IOException;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.TextField;
//
///**
// *
// * @author mgmoh
// */
//public class LoginController {
//
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private TextField passwordField;
//
//    private final UserModel userModel = new UserModel();
//
//    @FXML
//    public void handleLogin() throws IOException {
//        String username = usernameField.getText().trim();
//        String password = passwordField.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert(AlertType.ERROR, "Validation Error", "Username and password are required.");
//            return;
//        }
//
//        if (userModel.validateCredentials(username, password)) {
//            int roleId = userModel.getUserRole(username);
//            navigateToDashboard(roleId);
//        } else {
//            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
//        }
//    }
//
//    private void navigateToDashboard(int roleId) throws IOException {
//        String sceneName;
//        switch (roleId) {
//            case 1: sceneName = "admin-dashboard";
//            case 2: sceneName = "registrar-dashboard";
//            case 3: sceneName = "nurse-dashboard";
//            case 4: sceneName = "doctor-dashboard";
//            default: {
//                showAlert(AlertType.ERROR, "Access Denied", "Role not recognized.");
//                return;
//            }
//        }
//
//        SceneController.switchScene(sceneName);
//    }
//
//    private void showAlert(AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//}
