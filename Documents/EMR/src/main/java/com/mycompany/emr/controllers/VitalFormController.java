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
import com.mycompany.emr.models.Vital;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import utils.DatabaseConnection;

public class VitalFormController {

    private UserModel loggedInUser;

    public void setLoggedInUser(UserModel user) {
        this.loggedInUser = user;
        System.out.println("Logged-in user: " + user.getUsername());
    }

    @FXML
    private TableView<Vital> vitalsTable;

    @FXML
    private TableColumn<Vital, Integer> idColumn;

    @FXML
    private TableColumn<Vital, String> patientNameColumn;

    @FXML
    private TableColumn<Vital, Double> weightColumn;

    @FXML
    private TableColumn<Vital, Double> heightColumn;

    @FXML
    private TableColumn<Vital, String> bloodTypeColumn;

    @FXML
    private TableColumn<Vital, String> bloodPressureColumn;

    @FXML
    private TableColumn<Vital, Integer> heartRateColumn;

    @FXML
    private TableColumn<Vital, Double> temperatureColumn;

    @FXML
    private TableColumn<Vital, Double> bmiColumn;

    @FXML
    private TableColumn<Vital, String> complaintsColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField bloodPressureField;

    @FXML
    private TextField heartRateField;

    @FXML
    private TextField temperatureField;

    @FXML
    private Label bmiLabel;

    @FXML
    private TextArea complaintsField;

    @FXML
    private SplitMenuButton bloodTypeMenu;

    @FXML
    private Button saveButton;

    @FXML
    private Button signOutButton;

    private ObservableList<String> suggestions = FXCollections.observableArrayList();

    private ListView<String> suggestionListView = new ListView<>();

    private int selectedPatientId = -1;

    @FXML
    private AnchorPane parentPane;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        bloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        heartRateColumn.setCellValueFactory(new PropertyValueFactory<>("heartRate"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        bmiColumn.setCellValueFactory(new PropertyValueFactory<>("bmi"));
        complaintsColumn.setCellValueFactory(new PropertyValueFactory<>("complaints"));

        loadVitalData();

        searchField.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        searchField.setPromptText("Type patient name...");
        searchField.setPrefWidth(200);
        bloodTypeMenu.getItems().clear();
        bloodTypeMenu.getItems().addAll(
                new MenuItem("A+"), new MenuItem("A-"), new MenuItem("B+"), new MenuItem("B-"),
                new MenuItem("AB+"), new MenuItem("AB-"), new MenuItem("O+"), new MenuItem("O-")
        );

        bloodTypeMenu.getItems().forEach(item -> item.setOnAction(event -> bloodTypeMenu.setText(item.getText())));

        suggestionListView.setPrefHeight(120);
        suggestionListView.setVisible(false);
        parentPane.getChildren().add(suggestionListView);

        AnchorPane.setTopAnchor(suggestionListView, AnchorPane.getTopAnchor(searchField) + searchField.getHeight());
        AnchorPane.setLeftAnchor(suggestionListView, AnchorPane.getLeftAnchor(searchField));
        AnchorPane.setRightAnchor(suggestionListView, AnchorPane.getRightAnchor(searchField));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                suggestionListView.setVisible(false);
            } else {
                updateSuggestions(newValue);
                suggestionListView.setVisible(true);
            }
        });

        suggestionListView.setOnMouseClicked(event -> {
            String selected = suggestionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected);
                suggestionListView.setVisible(false);
                String[] parts = selected.split(" - ");
                selectedPatientId = Integer.parseInt(parts[0]);
                loadPatientDetails(selectedPatientId);
            }
        });

        saveButton.setOnAction(event -> saveVitals());

        signOutButton.setOnAction(event -> {
            try {
                handleSignOut();
            } catch (IOException ex) {
                Logger.getLogger(VitalFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        weightField.textProperty().addListener((observable, oldValue, newValue) -> calculateBMI());
        heightField.textProperty().addListener((observable, oldValue, newValue) -> calculateBMI());
    }

    private List<Vital> getAllVitals() {
        List<Vital> vitalsList = new ArrayList<>();
        String query = "SELECT v.id, p.name AS patientName, v.weight, v.height, v.blood_type, "
                + "v.blood_pressure, v.heart_rate, v.temperature, v.bmi, v.complaints "
                + "FROM Vitals v JOIN Patients p ON v.patient_id = p.id";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientName = resultSet.getString("patientName");
                double weight = resultSet.getDouble("weight");
                double height = resultSet.getDouble("height");
                String bloodType = resultSet.getString("blood_type");
                String bloodPressure = resultSet.getString("blood_pressure");
                int heartRate = resultSet.getInt("heart_rate");
                double temperature = resultSet.getDouble("temperature");
                double bmi = resultSet.getDouble("bmi");
                String complaints = resultSet.getString("complaints");

                vitalsList.add(new Vital(id, patientName, weight, height, bloodType,
                        bloodPressure, heartRate, temperature, bmi, complaints));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vitalsList;
    }

    private void loadVitalData() {
        List<Vital> vitals = getAllVitals();
        ObservableList<Vital> vitalObservableList = FXCollections.observableArrayList(vitals);

        if (vitalsTable != null) {
            vitalsTable.setItems(vitalObservableList);
        } else {
            System.err.println("vitalsTable is not initialized!");
        }
    }

    private void updateSuggestions(String query) {
        suggestions.clear();

        String sql = "SELECT id, name FROM Patients WHERE name LIKE ? OR id LIKE ? LIMIT 10";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            statement.setString(2, query + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String patient = resultSet.getInt("id") + " - " + resultSet.getString("name");
                suggestions.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        suggestionListView.setItems(suggestions);
    }

    private void loadPatientDetails(int patientId) {
        String sql = "SELECT * FROM Patients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Patient Selected: " + resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateBMI() {
        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            double bmi = weight / Math.pow(height / 100, 2);
            bmiLabel.setText(String.format("%.2f", bmi));
        } catch (NumberFormatException e) {
            bmiLabel.setText("Invalid");
        }
    }

    private void saveVitals() {
        if (selectedPatientId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "No patient selected.");
            return;
        }

        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            String bloodPressure = bloodPressureField.getText().trim();
            int heartRate = Integer.parseInt(heartRateField.getText().trim());
            double temperature = Double.parseDouble(temperatureField.getText().trim());
            String bmi = bmiLabel.getText();
            String bloodType = bloodTypeMenu.getText();
            String complaints = complaintsField.getText().trim();

            if (bloodType.equals("Select Blood Type")) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a valid blood type.");
                return;
            }

            String sql = "INSERT INTO Vitals (patient_id, weight, height, blood_pressure, heart_rate, temperature, bmi, blood_type, complaints) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, selectedPatientId);
                statement.setDouble(2, weight);
                statement.setDouble(3, height);
                statement.setString(4, bloodPressure);
                statement.setInt(5, heartRate);
                statement.setDouble(6, temperature);
                statement.setString(7, bmi);
                statement.setString(8, bloodType);
                statement.setString(9, complaints);

                statement.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vitals saved successfully.");
                loadVitalData();
                clearFields();

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save vitals. Please try again.");
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid values for weight, height, heart rate, and temperature.");
        }
    }

    private void clearFields() {
        searchField.clear();
        weightField.clear();
        heightField.clear();
        bloodPressureField.clear();
        heartRateField.clear();
        temperatureField.clear();
        bmiLabel.setText("");
        bloodTypeMenu.setText("Select Blood Type");
        complaintsField.clear();
    }

    private void handleSignOut() throws IOException {
        SceneController.switchScene("homepage");
        System.out.println("Sign-Out button clicked");

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
