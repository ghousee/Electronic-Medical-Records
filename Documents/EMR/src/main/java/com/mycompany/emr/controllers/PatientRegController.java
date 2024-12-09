/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.controllers;

import com.mycompany.emr.models.Patient;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DatabaseConnection;



public class PatientRegController {

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private SplitMenuButton genderMenu;

    @FXML
    private TextField telField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField kinContactField;

    @FXML
    private Button createPatientButton;

    @FXML
    private Button signOutButton;

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> idColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, LocalDate> dobColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, String> kinContactColumn;

    private void loadPatientData() {
        List<Patient> patients = getAllPatients();
        ObservableList<Patient> patientObservableList = FXCollections.observableArrayList(patients);
        patientTable.setItems(patientObservableList);
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        kinContactColumn.setCellValueFactory(new PropertyValueFactory<>("kinContact"));

        loadPatientData();

        genderMenu.getItems().clear();
        genderMenu.getItems().addAll(
                new MenuItem("Male"),
                new MenuItem("Female"),
                new MenuItem("Other")
        );

        genderMenu.getItems().forEach(item -> item.setOnAction(event -> genderMenu.setText(item.getText())));
        createPatientButton.setOnAction(event -> registerPatient());
        signOutButton.setOnAction(event -> {
            try {
                handleSignOut();
            } catch (IOException ex) {
                Logger.getLogger(PatientRegController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private ObservableList<Patient> getAllPatients() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();

        String query = "SELECT * FROM Patients";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                LocalDate dob = resultSet.getDate("dob") != null
                        ? resultSet.getDate("dob").toLocalDate()
                        : null;
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender") != null ? resultSet.getString("gender") : "N/A";
                String phone = resultSet.getString("phone_number") != null ? resultSet.getString("phone_number") : "N/A";
                String address = resultSet.getString("address") != null ? resultSet.getString("address") : "N/A";
                String kinContact = resultSet.getString("kin_contact") != null ? resultSet.getString("kin_contact") : "N/A";

                patients.add(new Patient(id, name, dob, age, gender, phone, address, kinContact));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private void registerPatient() {
        String name = nameField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String gender = genderMenu.getText();
        String tel = telField.getText().trim();
        String address = addressField.getText().trim();
        String kinContact = kinContactField.getText().trim();

        if (name.isEmpty() || dob == null || gender.equals("Select Gender") || tel.isEmpty() || address.isEmpty() || kinContact.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }
        int age = calculateAge(dob);
        String dobString = dob.toString();

        saveToDatabase(name, dobString, age, gender, tel, address, kinContact);
        loadPatientData();
    }

    private void saveToDatabase(String name, String dob, int age, String gender, String tel, String address, String kinContact) {
        String insertQuery = "INSERT INTO Patients (name, dob, age, gender, phone_number, address, kin_contact) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dob);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, tel);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, kinContact);

            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient registered successfully.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to register patient. Please try again.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        dobPicker.setValue(null);
        genderMenu.setText("Select Gender");
        telField.clear();
        addressField.clear();
        kinContactField.clear();
    }

    @FXML
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
