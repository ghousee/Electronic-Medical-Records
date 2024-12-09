/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.models;

public class Vital {

    private int id;
    private String patientName;
    private double weight;
    private double height;
    private String bloodType;
    private String bloodPressure;
    private int heartRate;
    private double temperature;
    private double bmi;
    private String complaints;

    public Vital(int id, String patientName, double weight, double height, String bloodType,
            String bloodPressure, int heartRate, double temperature, double bmi, String complaints) {
        this.id = id;
        this.patientName = patientName;
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.bmi = bmi;
        this.complaints = complaints;
    }

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getBmi() {
        return bmi;
    }

    public String getComplaints() {
        return complaints;
    }

}
