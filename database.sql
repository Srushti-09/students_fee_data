

CREATE DATABASE IF NOT EXISTS school_fee_tracker;
USE school_fee_tracker;

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    class VARCHAR(20),
    contact VARCHAR(15),
    total_fees DECIMAL(10,2),
    fees_paid DECIMAL(10,2),
    pending_fees DECIMAL(10,2) GENERATED ALWAYS AS (total_fees - fees_paid) STORED,
    PRIMARY KEY (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS payments;

CREATE TABLE payments (
    payment_id INT NOT NULL AUTO_INCREMENT,
    student_id INT,
    amount DECIMAL(10,2),
    payment_date DATE,
    PRIMARY KEY (payment_id),
    KEY student_id (student_id),
    CONSTRAINT payments_ibfk_1 FOREIGN KEY (student_id) 
        REFERENCES students (student_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
