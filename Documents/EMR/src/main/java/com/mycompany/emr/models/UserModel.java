/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Ghouse
 */
package com.mycompany.emr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseConnection;

public class UserModel {

    private int id;
    private String username;
    private String role;

    public UserModel() {
    }

    public UserModel(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean registerUser(String username, String password, int roleId) {
        String sql = "INSERT INTO Users (username, password, role_id, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, roleId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public int getUserIdByUsername(String username) {
        String query = "SELECT id FROM Users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
        }

        return 0; 
    }

    public boolean checkUserExists(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    public boolean validateCredentials(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Users retrieved");
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error validating credentials: " + e.getMessage());
            return false;
        }
    }

    public int getUserRole(String username) {
        String sql = "SELECT role_id FROM Users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("role_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user role: " + e.getMessage());
        }
        return 0;
    }

    public List<UserModel> getAllUsers() throws SQLException {
        List<UserModel> users = new ArrayList<>();
        for (UserModel user : users) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + ", Role: " + user.getRole());
        }

        String query = "SELECT u.id, u.username, r.name AS role FROM Users u JOIN Roles r ON u.role_id = r.id";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");

                System.out.println("Fetched User -> ID: " + id + ", Username: " + username + ", Role: " + role);

                users.add(new UserModel(id, username, role));
            }
        }

        return users;
    }

    public void deleteUser(int userId) {
        String deleteQuery = "DELETE FROM Users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {            
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();
            System.out.println("User deleted successfully.");

            resetUserIds();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    public void updateUser(int userId, String newUsername, String newRole) throws SQLException {
        String query = "UPDATE Users SET username = ?, role_id = (SELECT id FROM Roles WHERE name = ?) WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newUsername);
            statement.setString(2, newRole);
            statement.setInt(3, userId);
            statement.executeUpdate();
        }
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

    public void resetUserIds() {
        String resetIdsQuery = "SET @row_number = 0; UPDATE Users SET id = (@row_number := @row_number + 1) ORDER BY id;";
        String resetAutoIncrementQuery = "ALTER TABLE Users AUTO_INCREMENT = 1;";

        try (Connection connection = DatabaseConnection.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("SET @row_number = 0;"); 
            statement.execute(resetIdsQuery); 
            statement.execute(resetAutoIncrementQuery); 
            System.out.println("User IDs reset successfully.");
        } catch (SQLException e) {
            System.err.println("Error resetting user IDs: " + e.getMessage());
        }
    }

}
