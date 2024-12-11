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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomePageController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private UserModel loggedInUser;

    private final UserModel userModel = new UserModel();

    public UserModel getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and password are required.");
            return;
        }

        if (userModel.validateCredentials(username, password)) {
            int userId = userModel.getUserIdByUsername(username); 
            int roleId = userModel.getUserRole(username);

            if (userId == 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch user ID.");
                return;
            }

            String roleName = getRoleNameById(roleId);
            UserModel loggedInUser = new UserModel(userId, username, roleName);
            SessionManager.setLoggedInUser(loggedInUser);

            System.out.println("Logged-In User ID: " + loggedInUser.getId());
            System.out.println("Logged-In User Role: " + loggedInUser.getRole());

            navigateToDashboard(roleId);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void navigateToDashboard(int roleId) throws IOException {
        FXMLLoader loader = null;

        switch (roleId) {
            case 1: 
                loader = new FXMLLoader(getClass().getResource("/com/mycompany/emr/admin-dashboard.fxml"));
                break;
            case 2: 
                loader = new FXMLLoader(getClass().getResource("/com/mycompany/emr/patient-registration.fxml"));
                break;
            case 3: 
                loader = new FXMLLoader(getClass().getResource("/com/mycompany/emr/Vital_Form.fxml"));
                break;
            case 4: 
                loader = new FXMLLoader(getClass().getResource("/com/mycompany/emr/DoctorForm.fxml"));
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Access Denied", "Role not recognized.");
                return;
        }

        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof AdminDashboardController) {
            ((AdminDashboardController) controller).setLoggedInUser(SessionManager.getLoggedInUser());
        } else if (controller instanceof VitalFormController) {
            ((VitalFormController) controller).setLoggedInUser(SessionManager.getLoggedInUser());
        } else if (controller instanceof AdminDashboardController) {
            ((AdminDashboardController) controller).setLoggedInUser(SessionManager.getLoggedInUser());
        } else if (controller instanceof DoctorFormController) {
            ((DoctorFormController) controller).setLoggedInUser(SessionManager.getLoggedInUser());
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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

    private String getRoleNameById(int roleId) {
        switch (roleId) {
            case 1:
                return "Admin";
            case 2:
                return "Registration Officer";
            case 3:
                return "Nurse";
            case 4:
                return "Doctor";
            default:
                return "Unknown";
        }
    }
}
