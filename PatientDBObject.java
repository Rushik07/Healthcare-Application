package com.example.projectwork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatientDBObject {
    ExecutorService executorService;

    public PatientDBObject() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public synchronized List<Patients.Patient> getAllPatients() throws SQLException {
        List<Patients.Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        System.out.println("Trying to get patient records");

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String medicalHistory = resultSet.getString("medical_history");
                patients.add(new Patients.Patient(name, age, medicalHistory));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return patients;
    }

    public synchronized void saveOrUpdatePatient(Patients.Patient patient) throws SQLException {
        String findQuery = "SELECT COUNT(*) FROM patients WHERE name = ?";
        String insertQuery = "INSERT INTO patients (name, age, medical_history) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE patients SET age = ?, medical_history = ? WHERE name = ?";

        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setString(1, patient.getName());

            ResultSet resultSet = findStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, patient.getAge());
                    updateStatement.setString(2, patient.getMedicalHistory());
                    updateStatement.setString(3, patient.getName());
                    updateStatement.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, patient.getName());
                    insertStatement.setInt(2, patient.getAge());
                    insertStatement.setString(3, patient.getMedicalHistory());
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}