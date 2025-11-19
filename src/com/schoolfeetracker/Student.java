package com.schoolfeetracker;

import javafx.beans.property.*;

public class Student {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty studentClass;
    private StringProperty contact;
    private DoubleProperty totalFees;
    private DoubleProperty feesPaid;
    private DoubleProperty pendingFees;

    public Student(int id, String name, String studentClass, String contact,
                   double totalFees, double feesPaid, double pendingFees) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.studentClass = new SimpleStringProperty(studentClass);
        this.contact = new SimpleStringProperty(contact);
        this.totalFees = new SimpleDoubleProperty(totalFees);
        this.feesPaid = new SimpleDoubleProperty(feesPaid);
        this.pendingFees = new SimpleDoubleProperty(pendingFees);
    }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getStudentClass() { return studentClass.get(); }
    public String getContact() { return contact.get(); }
    public double getTotalFees() { return totalFees.get(); }
    public double getFeesPaid() { return feesPaid.get(); }
    public double getPendingFees() { return pendingFees.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty studentClassProperty() { return studentClass; }
    public StringProperty contactProperty() { return contact; }
    public DoubleProperty totalFeesProperty() { return totalFees; }
    public DoubleProperty feesPaidProperty() { return feesPaid; }
    public DoubleProperty pendingFeesProperty() { return pendingFees; }
}
