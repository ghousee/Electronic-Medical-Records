/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.controllers;

import com.mycompany.emr.models.UserModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private TableView<UserModel> userTable;

    @FXML
    private TableColumn<UserModel, Integer> idColumn;

    @FXML
    private TableColumn<UserModel, String> usernameColumn;

    @FXML
    private TableColumn<UserModel, String> roleColumn;

    @FXML
    private TableColumn<UserModel, Void> actionsColumn;

    private UserModel loggedInUser;

    public void setLoggedInUser(UserModel user) {
        this.loggedInUser = user;
        System.out.println("Logged-in user set to: " + user.getUsername());
    }

    @FXML
    private Button addUserButton;

    @FXML
    private Button editUserButton;

    @FXML
    private Button deleteUserButton;

    private final UserModel userModel = new UserModel();
    private ObservableList<UserModel> userData = FXCollections.observableArrayList();

    private UserModel getCurrentLoggedInUser() {
        return SessionManager.getLoggedInUser();
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

    private int getRoleIdByName(String roleName) {
        switch (roleName.toLowerCase()) {
            case "admin":
                return 1;
            case "registration officer":
                return 2;
            case "nurse":
                return 3;
            case "doctor":
                return 4;
            default:
                return 0;
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        addActionButtons();

        try {
            List<UserModel> users = userModel.getAllUsers();
            ObservableList<UserModel> userList = FXCollections.observableArrayList(users);
            userTable.setItems(userList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user data.");
        }

    }

    private void addActionButtons() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setOnAction(event -> {
                    UserModel user = getTableView().getItems().get(getIndex());
                    confirmDelete(user);
                });

                editButton.setOnAction(event -> {
                    UserModel user = getTableView().getItems().get(getIndex());
                    openEditDialog(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ButtonBar buttonBar = new ButtonBar();
                    buttonBar.getButtons().addAll(editButton, deleteButton);
                    setGraphic(buttonBar);
                }
            }
        });
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadUserData() {
        try {
            ObservableList<UserModel> users = FXCollections.observableArrayList(userModel.getAllUsers());
            userTable.setItems(users);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        Stage stage = new Stage();
        stage.setTitle("Add User");

        VBox addForm = new VBox(10);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter Password");
        TextField roleField = new TextField();
        roleField.setPromptText("Enter Role (e.g., Admin, Nurse, Doctor, Registration Officer)");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return;
            }

            if (role.equalsIgnoreCase("Admin")) {
                UserModel currentUser = getCurrentLoggedInUser();
                if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("Admin")) {
                    showAlert(Alert.AlertType.ERROR, "Permission Denied", "Only an Admin can create another Admin.");
                    return;
                }
            }

            try {
                int roleId = getRoleIdByName(role);
                if (roleId == 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Role", "The specified role does not exist.");
                    return;
                }

                boolean success = userModel.registerUser(username, password, roleId);
                if (success) {
                    userModel.resetUserIds();
                    loadUserData();
                    stage.close();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the user.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the user.");
                e.printStackTrace();
            }
        });

        cancelButton.setOnAction(event -> stage.close());

        addForm.getChildren().addAll(usernameField, passwordField, roleField, new HBox(10, saveButton, cancelButton));
        Scene scene = new Scene(addForm, 300, 200);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void openEditDialog(UserModel user) {
        Stage stage = new Stage();
        stage.setTitle("Edit User");

        VBox editForm = new VBox(10);
        TextField usernameField = new TextField(user.getUsername());
        TextField roleField = new TextField(user.getRole());
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            String newUsername = usernameField.getText().trim();
            String newRole = roleField.getText().trim();

            if (newUsername.isEmpty() || newRole.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Fields cannot be empty.");
                return;
            }

            try {
                userModel.updateUser(user.getId(), newUsername, newRole);
                loadUserData();
                stage.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.");
                e.printStackTrace();
            }
        });

        cancelButton.setOnAction(event -> stage.close());

        editForm.getChildren().addAll(usernameField, roleField, new HBox(10, saveButton, cancelButton));
        Scene scene = new Scene(editForm, 300, 200);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void confirmDelete(UserModel user) {
        UserModel loggedInUser = SessionManager.getLoggedInUser();

        if (loggedInUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Logged-in user not found.");
            return;
        }

        System.out.println("Logged-In User ID: " + loggedInUser.getId());
        System.out.println("Target User ID: " + user.getId());

        if (loggedInUser.getId() == user.getId()) {
            showAlert(Alert.AlertType.WARNING, "Action Forbidden", "You cannot delete your own account.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Delete");
        confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
        confirmationAlert.setContentText("User: " + user.getUsername());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userModel.deleteUser(user.getId());
                    userModel.resetUserIds();
                    loadUserData();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSignOut() throws IOException {
        SceneController.switchScene("homepage");
        System.out.println("Sign-Out button clicked");
    }

    @FXML
    private void handleReloadDB() {
        loadUserData();
        System.out.println("Database fetched(reloaded).");
    }

}
