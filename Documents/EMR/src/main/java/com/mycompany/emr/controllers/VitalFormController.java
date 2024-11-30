/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.emr.controllers;

import com.mycompany.emr.App;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author mgmoh
 */
public class VitalFormController {

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Link back button to the login page
        backButton.setOnAction(event -> {
            try {
                SceneController.switchScene("homepage");
            } catch (IOException ex) {
                Logger.getLogger(VitalFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
