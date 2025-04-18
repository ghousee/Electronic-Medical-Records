# Electronic Medical Records (EMR) System

A Java-based desktop application designed to help hospitals and clinics manage patient records efficiently with role-based access control. This project was built as part of a coursework initiative and simulates a real-world clinical environment with separate modules for Admins, Registration Officers, Nurses, and Doctors.

## Features

- **Role-Based Access**  
  Different users have different levels of access to protect sensitive data and streamline workflows:
  - Admin: Manage users and system-wide settings  
  - Registration Officer: Add new patients and update demographics  
  - Nurse: Record vitals and observations  
  - Doctor: Add diagnoses, prescribe medication, and leave comments

- **Patient Data Management**  
  Add, view, and update patient records securely. All data is stored in a MySQL database using structured schemas.

- **Form-Based UI**  
  Built with Java Swing for an interactive, desktop-friendly experience. Includes custom input forms, validation, and smooth panel navigation.

- **Separation of Concerns**  
  Utilizes the DAO (Data Access Object) pattern to separate business logic from database operations, ensuring cleaner, maintainable code.

## Tech Stack

- **Language**: Java  
- **GUI Framework**: Java Swing  
- **Database**: MySQL  
- **Database Connectivity**: JDBC  
- **Architecture**: DAO pattern  
- **IDE**: NetBeans  
- **Version Control**: Git & GitHub

## 🙌 Acknowledgements

Built as part of a university course to simulate how real-world medical record systems function with security, modularity, and maintainability in mind.

