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
import com.mycompany.emr.models.Vital;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.DatabaseConnection;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.File; 
import javafx.stage.FileChooser;
import javafx.stage.Stage; 

public class DoctorFormController {

    private UserModel loggedInUser;

    public void setLoggedInUser(UserModel user) {
        this.loggedInUser = user;
        System.out.println("Logged-in user set to: " + user.getUsername());
    }

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> suggestionListView;

    @FXML
    private TableView<Vital> vitalsTable;

    @FXML
    private TableColumn<Vital, String> vitalNameColumn;

    @FXML
    private TableColumn<Vital, String> vitalBloodTypeColumn;

    @FXML
    private TableColumn<Vital, String> vitalBloodPressureColumn;

    @FXML
    private TableColumn<Vital, Double> vitalWeightColumn;

    @FXML
    private TableColumn<Vital, Double> vitalHeightColumn;

    @FXML
    private TableColumn<Vital, Double> vitalBMIColumn;

    @FXML
    private TableColumn<Vital, Integer> vitalHeartRateColumn;

    @FXML
    private TableColumn<Vital, Double> vitalTemperatureColumn;

    @FXML
    private Label complaintsLabel;

    @FXML
    private TextArea diagnosticsArea;

    @FXML
    private ComboBox<String> medicationsComboBox;

    @FXML
    private Button saveButton;

    private int selectedPatientId = -1;

    private ObservableList<String> suggestions = FXCollections.observableArrayList();

    @FXML
    private AnchorPane parentPane;

    @FXML
    private Button downloadPdfButton;

    @FXML
    public void initialize() {
        vitalNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatientName()));
        vitalBloodTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBloodType()));
        vitalBloodPressureColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBloodPressure()));
        vitalWeightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getWeight()).asObject());
        vitalHeightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getHeight()).asObject());
        vitalBMIColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBmi()).asObject());
        vitalHeartRateColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getHeartRate()).asObject());
        vitalTemperatureColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTemperature()).asObject());

        if (suggestionListView == null) {
            throw new RuntimeException("suggestionListView is not initialized. Check your FXML file.");
        }

        suggestionListView.setVisible(false);

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

                loadVitals(selectedPatientId);
            }
        });

        medicationsComboBox.setItems(FXCollections.observableArrayList(
                "Paracetamol", "Ibuprofen", "Amoxicillin", "Cough Syrup", "Antibiotics"
        ));

        saveButton.setOnAction(event -> saveConsultation());
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

    private void handlePatientSelection() {
        String selected = suggestionListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            searchField.setText(selected);
            suggestionListView.setVisible(false);

            String[] parts = selected.split(" - ");
            selectedPatientId = Integer.parseInt(parts[0]);

            loadVitals(selectedPatientId);
        }
    }

    private void loadVitals(int patientId) {
        ObservableList<Vital> vitals = FXCollections.observableArrayList();

        String query = "SELECT v.*, p.name AS patientName FROM Vitals v JOIN Patients p ON v.patient_id = p.id WHERE v.patient_id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                vitals.add(new Vital(
                        resultSet.getInt("id"),
                        resultSet.getString("patientName"),
                        resultSet.getDouble("weight"),
                        resultSet.getDouble("height"),
                        resultSet.getString("blood_type"),
                        resultSet.getString("blood_pressure"),
                        resultSet.getInt("heart_rate"),
                        resultSet.getDouble("temperature"),
                        resultSet.getDouble("bmi"),
                        resultSet.getString("complaints")
                ));
            }
            if (!vitals.isEmpty()) {
                complaintsLabel.setText(vitals.get(vitals.size() - 1).getComplaints());
            } else {
                complaintsLabel.setText("No complaints available."); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        vitalsTable.setItems(vitals);
    }

    private void saveConsultation() {
        if (selectedPatientId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a patient.");
            return;
        }

        String diagnostics = diagnosticsArea.getText();
        String medication = medicationsComboBox.getValue();

        if (diagnostics.isEmpty() || medication == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter diagnostics and select a medication.");
            return;
        }

        String query = "INSERT INTO Consultation (patient_id, diagnostics, medications) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, selectedPatientId);
            statement.setString(2, diagnostics);
            statement.setString(3, medication);

            statement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Consultation saved successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save consultation.");
        }
    }

    private void clearFields() {
        searchField.clear();
        suggestionListView.setItems(FXCollections.observableArrayList());
        vitalsTable.setItems(FXCollections.observableArrayList());
        diagnosticsArea.clear();
        medicationsComboBox.setValue(null);
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
    private void downloadPDF() throws Exception {
        System.out.println("dowloadPDF");
        if (selectedPatientId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a patient to generate the PDF.");
            return;
        }

        String patientName = searchField.getText();
        ObservableList<Vital> vitals = vitalsTable.getItems();

        if (vitals.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No vitals available to generate the PDF.");
            return;
        }

        String diagnostics = diagnosticsArea.getText();
        String medication = medicationsComboBox.getValue();

        if (diagnostics.isEmpty() || medication == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter diagnostics and select a medication.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Patient_Report_" + patientName.replaceAll("\\s", "_") + ".pdf");
        File file = fileChooser.showSaveDialog(new Stage());
        
        try {
            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Patient Report")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Patient Name: " + patientName));
            document.add(new Paragraph("Selected Patient ID: " + selectedPatientId));

            document.add(new Paragraph("\nVitals:").setBold());
            for (Vital vital : vitals) {
                document.add(new Paragraph(
                        "Weight: " + vital.getWeight()
                        + ", Height: " + vital.getHeight()
                        + ", Blood Type: " + vital.getBloodType()
                        + ", Blood Pressure: " + vital.getBloodPressure()
                        + ", Heart Rate: " + vital.getHeartRate()
                        + ", Temperature: " + vital.getTemperature()
                        + ", BMI: " + vital.getBmi()
                        + ", Complaints: " + vital.getComplaints()
                ));
            }

            document.add(new Paragraph("\nDoctor's Diagnostics:").setBold());
            document.add(new Paragraph(diagnostics));
            document.add(new Paragraph("\nPrescribed Medications:").setBold());
            document.add(new Paragraph(medication));

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "PDF Generated", "The PDF report has been successfully saved at:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate PDF: " + e.getMessage());
        }
    }

}
