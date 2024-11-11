/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.lab06_jdbc;

import java.util.List;
import javax.swing.SwingUtilities;
import models.Product;
import models.Student;
import utils.DatabaseUtils;

/**
 *
 * @author mgmoh
// */
//public class LAB06_JDBC {
//
//    public static void main(String[] args) {
//        DatabaseUtils dbUtils = new DatabaseUtils();
//        
//        List<Student> students = dbUtils.getStudents();
//        
//        for(Student student: students){
//            System.out.println(student.toString());
//        }
//    }
//}


public class LAB06_JDBC {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}