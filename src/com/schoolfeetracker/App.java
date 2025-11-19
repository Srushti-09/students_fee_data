package com.schoolfeetracker;

public class App {
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();

        // 1. Add a new student
        db.addStudent("Tanmay Mahajan", "12A", "9876543210", 50000);

        // 2. Record a payment for that student (use student_id = 1 for testing)
        db.recordPayment(1, 20000);

        // 3. Fetch and print pending fees
        double pending = db.getPendingFees(1);
        if (pending >= 0) {
            System.out.println("Pending fees for student ID 1: Rs." + pending);
        } else {
            System.out.println("Could not retrieve pending fees.");
        }

        // 4. Close connection
        db.close();

        System.out.println("Program finished successfully!");
    }
}
