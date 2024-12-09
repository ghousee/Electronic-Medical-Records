/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.models;

import java.time.LocalDate;

public class Patient {

    private int id;
    private String name;
    private LocalDate dob;
    private int age;
    private String gender;
    private String phoneNumber;
    private String address;
    private String kinContact;

    public Patient(int id, String name, LocalDate dob, int age, String gender, String phoneNumber, String address, String kinContact) {
        this.id = id;
        this.name = name != null ? name : "N/A";
        this.dob = dob;
        this.age = age;
        this.gender = gender != null ? gender : "N/A";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "N/A";
        this.address = address != null ? address : "N/A";
        this.kinContact = kinContact != null ? kinContact : "N/A";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getKinContact() {
        return kinContact;
    }
}
