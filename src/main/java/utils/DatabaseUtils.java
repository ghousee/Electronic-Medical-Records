/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import models.Product;
import models.Student;

/**
 *
 * @author mgmoh
 */
public class DatabaseUtils {
    
    String user = "root";
    String pass = "admin";
    
    String connStr = "jdbc:mysql://localhost:8000/products";
    Connection conn;
    public DatabaseUtils() {
        try{
            conn = DriverManager.getConnection(connStr, user, pass);
            System.out.println("Connection Successful.");
        } catch (SQLException ex){
            System.out.println("Connection unsuccessful." + ex.getMessage());
        }
    
    }

    public boolean studentExists(String email) {
        String query = "SELECT COUNT(*) FROM Student WHERE Email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error checking student existence: " + ex.getMessage());
        }
        return false;
    }
    
public void updateStudent(int id, String name, String email, int age) {
    try {
        String sql = "UPDATE Student SET Name = ?, Email = ?, Age = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setInt(3, age);
        stmt.setInt(4, id);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Student updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No student found with the given ID.");
        }
    } catch (SQLException ex) {
                ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating student: " + ex.getMessage());
    }
}
public void deleteStudent(int id) {
    try {
        String sql = "DELETE FROM Student WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Student deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No student found with the given ID.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error deleting student: " + ex.getMessage());
    }
}

private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    return email.matches(emailRegex);
}

    public void createStudent(String name, String email, int age){    
    
    if (name == null || name.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.");
        return;
    }

    if (email == null || email.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Email cannot be empty. Please enter a valid email.");
        return;
    }

    if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(null, "Please enter a valid email format (e.g., user@example.com).");
        return;
    }

    if (age <= 0) {
        JOptionPane.showMessageDialog(null, "Please enter a valid positive age.");
        return;
    }
        if (studentExists(email)) {
            JOptionPane.showMessageDialog(null, "A student with the same email already exists.");
            return;
        }
        try {
     
            String sql = "INSERT INTO Student (Name, Email, Age) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, age);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Student added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding student.");
        }
    }
    
    public List<Student> getStudents(){
        List<Student> students = new ArrayList<>();
        String QUERY = "SELECT * FROM Student";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            
            while(rs.next()){
                Student student = new Student(rs.getInt("id"),rs.getString("Name"), rs.getString("Email"), rs.getString("Age"));
                students.add(student);
                
            }
            stmt.close();
        } catch (SQLException ex){
            System.out.println("Error while fetching data..." + ex.getMessage());
        }
        return students;
    }
}
