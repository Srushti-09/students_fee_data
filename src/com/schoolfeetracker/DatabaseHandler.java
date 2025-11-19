package com.schoolfeetracker;

import java.sql.*;

public class DatabaseHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/school_fee_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "Srushti123#";

    private Connection conn;

    public DatabaseHandler() {
        try {
            // Load MySQL driver explicitly (optional for modern JDBC, but safer)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    // Add new student
    public void addStudent(String name, String studentClass, String contact, double totalFees) {
        String query = "INSERT INTO students (name, class, contact, total_fees, fees_paid) VALUES (?, ?, ?, ?, 0)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, studentClass);
            stmt.setString(3, contact);
            stmt.setDouble(4, totalFees);
            stmt.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete student
    public void deleteStudent(int studentId) {
    try {
        // Delete payments for this student first
        String deletePayments = "DELETE FROM payments WHERE student_id = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(deletePayments)) {
            stmt1.setInt(1, studentId);
            stmt1.executeUpdate();
        }

        // Then delete the student
        String deleteStudent = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement stmt2 = conn.prepareStatement(deleteStudent)) {
            stmt2.setInt(1, studentId);
            stmt2.executeUpdate();
        }

        System.out.println("Student deleted successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Record a payment
    public void recordPayment(int studentId, double amount) {
        String updateStudent = "UPDATE students SET fees_paid = fees_paid + ? WHERE student_id = ?";
        String insertPayment = "INSERT INTO payments (student_id, amount, payment_date) VALUES (?, ?, CURDATE())";

        try (PreparedStatement stmt1 = conn.prepareStatement(updateStudent);
             PreparedStatement stmt2 = conn.prepareStatement(insertPayment)) {

            stmt1.setDouble(1, amount);
            stmt1.setInt(2, studentId);
            stmt1.executeUpdate();

            stmt2.setInt(1, studentId);
            stmt2.setDouble(2, amount);
            stmt2.executeUpdate();

            System.out.println("Payment recorded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculate pending fees
    public double getPendingFees(int studentId) {
        String query = "SELECT (total_fees - fees_paid) AS pending FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("pending");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Connection getConnection() {
        return conn;
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
