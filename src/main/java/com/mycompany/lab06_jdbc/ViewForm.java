/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.lab06_jdbc;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Student;
import utils.DatabaseUtils;

/**
 *
 * @author mgmoh
 */
public class ViewForm extends javax.swing.JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseUtils dbUtils;


    /**
     * Creates new form ViewForm
     */
    public ViewForm(JPanel bottomJPanel) {
        initComponents();
        dbUtils = new DatabaseUtils();

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Age"}, 0);
        table = new JTable(tableModel);

        table.setPreferredScrollableViewportSize(panelJTable.getSize());
        table.setFillsViewportHeight(true);
        
        loadStudents();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(panelJTable.getSize());

        panelJTable.removeAll();
        panelJTable.add(scrollPane);
        panelJTable.revalidate();
        panelJTable.repaint();

    }
    private void loadStudents() {
        List<Student> students = dbUtils.getStudents();
        tableModel.setRowCount(0); // Clear the table before loading data

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getAge()
            });
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    
    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
    String currentName = (String) tableModel.getValueAt(selectedRow, 1);
    String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
    String currentAgeStr = tableModel.getValueAt(selectedRow, 3).toString();

String newName;
    while (true) {
        newName = JOptionPane.showInputDialog(this, "Enter new name:", currentName);
        if (newName == null) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty. Please enter a valid name.");
            return; // Exit if the user cancels
        }
        if (!newName.trim().isEmpty()) {
            break;
        }
        JOptionPane.showMessageDialog(this, "Name cannot be empty. Please enter a valid name.");
    }

    // Pop-up for Email
    String newEmail;
    while (true) {
        newEmail = JOptionPane.showInputDialog(this, "Enter new email:", currentEmail);
        if (newEmail == null) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty. Please enter a valid email.");
            return; // Exit if the user cancels
        }
        if (!newEmail.trim().isEmpty()) {
            if (isValidEmail(newEmail)) {
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid email format (e.g., user@example.com).");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Email cannot be empty. Please enter a valid email.");
        }
    }

    // Pop-up for Age
    String newAgeStr;
    int newAge;
    while (true) {
        newAgeStr = JOptionPane.showInputDialog(this, "Enter new age:", currentAgeStr);
        if (newAgeStr == null) {
            JOptionPane.showMessageDialog(this, "Age cannot be empty. Please enter a valid age.");
            return; // Exit if the user cancels
        }
        if (!newAgeStr.trim().isEmpty()) {
            try {
                newAge = Integer.parseInt(newAgeStr);
                if (newAge > 0) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive integer for age.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer for age.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Age cannot be empty. Please enter a valid age.");
        }
    }

    // Check if the new name or email already exists (excluding the current student)
    if (dbUtils.studentExists(newEmail) && (!currentEmail.equals(newEmail))) {
        JOptionPane.showMessageDialog(this, "A student with the same name or email already exists.");
        return;
    }
        dbUtils.updateStudent(id, newName, newEmail, newAge);
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        loadStudents();
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        panelJTable = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();

        jButton1.setText("jButton1");

        jLabel1.setText("STUDENT DETAILS");

        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        panelJTable.setLayout(new java.awt.BorderLayout());

        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(110, 110, 110)
                .addComponent(btnEdit)
                .addGap(18, 18, 18)
                .addComponent(btnDelete)
                .addContainerGap(24, Short.MAX_VALUE))
            .addComponent(panelJTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelJTable, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        updateStudent();
        loadStudents(); // Refresh the table after update
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        return;
    }

    int id = (int) tableModel.getValueAt(selectedRow, 0);

    // Confirm the delete action
    int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirmation == JOptionPane.YES_OPTION) {
        dbUtils.deleteStudent(id);
        loadStudents(); // Refresh the JTable data after deletion
    }
        loadStudents();
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelJTable;
    // End of variables declaration//GEN-END:variables
}
