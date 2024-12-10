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

public class SessionManager {

    private static UserModel loggedInUser;

    public static UserModel getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(UserModel user) {
        loggedInUser = user;
    }

}
