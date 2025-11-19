package com.schoolfeetracker;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Frontend extends Application {

    private TableView<Student> table = new TableView<>();
    private DatabaseHandler db = new DatabaseHandler();

    @Override
    public void start(Stage stage) {
        // Form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField classField = new TextField();
        classField.setPromptText("Class");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact");
        TextField totalFeesField = new TextField();
        totalFeesField.setPromptText("Total Fees");

        Button addBtn = new Button("Add Student");
        Button deleteBtn = new Button("Delete Selected");

        HBox form = new HBox(10, nameField, classField, contactField, totalFeesField, addBtn, deleteBtn);
        form.setPadding(new Insets(10));

        // Table columns
        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Student, String> classCol = new TableColumn<>("Class");
        classCol.setCellValueFactory(data -> data.getValue().studentClassProperty());

        TableColumn<Student, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(data -> data.getValue().contactProperty());

        TableColumn<Student, Double> totalFeesCol = new TableColumn<>("Total Fees");
        totalFeesCol.setCellValueFactory(data -> data.getValue().totalFeesProperty().asObject());

        TableColumn<Student, Double> feesPaidCol = new TableColumn<>("Fees Paid");
        feesPaidCol.setCellValueFactory(data -> data.getValue().feesPaidProperty().asObject());

        TableColumn<Student, Double> pendingCol = new TableColumn<>("Pending Fees");
        pendingCol.setCellValueFactory(data -> data.getValue().pendingFeesProperty().asObject());

        table.getColumns().addAll(idCol, nameCol, classCol, contactCol, totalFeesCol, feesPaidCol, pendingCol);
        table.setItems(getStudentsFromDB());

        // Add student
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String studentClass = classField.getText();
                String contact = contactField.getText();
                double totalFees = Double.parseDouble(totalFeesField.getText());

                db.addStudent(name, studentClass, contact, totalFees);
                table.setItems(getStudentsFromDB());

                nameField.clear();
                classField.clear();
                contactField.clear();
                totalFeesField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input!");
                alert.showAndWait();
            }
        });

        // Delete student
        deleteBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                db.deleteStudent(selected.getId());
                table.setItems(getStudentsFromDB());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select a student first!");
                alert.showAndWait();
            }
        });

        // Payment fields
        TextField paymentField = new TextField();
        paymentField.setPromptText("Payment Amount");
        Button payBtn = new Button("Pay Fees");

        HBox paymentBox = new HBox(10, paymentField, payBtn);
        paymentBox.setPadding(new Insets(10));

        payBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select a student first!");
                alert.showAndWait();
                return;
            }

            try {
                double amount = Double.parseDouble(paymentField.getText());
                db.recordPayment(selected.getId(), amount);
                table.setItems(getStudentsFromDB());
                paymentField.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enter a valid number!");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(10, form, table, paymentBox);
        Scene scene = new Scene(root, 950, 550);

        stage.setScene(scene);
        stage.setTitle("School Fee Tracker");
        stage.show();
    }

    private ObservableList<Student> getStudentsFromDB() {
        ObservableList<Student> list = FXCollections.observableArrayList();
        try {
            ResultSet rs = db.getConnection().createStatement().executeQuery("SELECT * FROM students");

            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                String studentClass = rs.getString("class");
                String contact = rs.getString("contact");
                double totalFees = rs.getDouble("total_fees");
                double feesPaid = rs.getDouble("fees_paid");
                double pending = totalFees - feesPaid;

                list.add(new Student(id, name, studentClass, contact, totalFees, feesPaid, pending));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
