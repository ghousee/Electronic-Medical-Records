/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author ebokaren
 */
//public class DatabaseConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/EMR";
//    private static final String USER = "root"; 
//    private static final String PASSWORD = "password"; 
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//}

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:8000/EMR";
    private static final String USER = "root"; 
    private static final String PASSWORD = "pass"; 

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
