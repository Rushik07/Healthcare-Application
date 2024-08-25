package com.example.projectwork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentDBObject {
    ExecutorService executorService;

    public AppointmentDBObject() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public synchronized List<Appointments.Appointment> getAllAppointments() throws SQLException {
        List<Appointments.Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String patientName = resultSet.getString("name");
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String reason = resultSet.getString("appointment_reason");
                appointments.add(new Appointments.Appointment(patientName, date, time, reason));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return appointments;
    }

    public synchronized void saveOrUpdateAppointment(Appointments.Appointment appointment) throws SQLException {
        String findQuery = "SELECT COUNT(*) FROM appointments WHERE name = ? AND appointment_date = ? AND appointment_time = ?";
        String insertQuery = "INSERT INTO appointments (name, appointment_date, appointment_time, appointment_reason) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE appointments SET appointment_reason = ? WHERE name = ? AND appointment_date = ? AND appointment_time = ?";

        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement findStatement = connection.prepareStatement(findQuery);
            findStatement.setString(1, appointment.getPatientName());
            findStatement.setString(2, appointment.getDate());
            findStatement.setString(3, appointment.getTime());

            ResultSet resultSet = findStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, appointment.getReason());
                    updateStatement.setString(2, appointment.getPatientName());
                    updateStatement.setString(3, appointment.getDate());
                    updateStatement.setString(4, appointment.getTime());
                    updateStatement.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, appointment.getPatientName());
                    insertStatement.setString(2, appointment.getDate());
                    insertStatement.setString(3, appointment.getTime());
                    insertStatement.setString(4, appointment.getReason());
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public synchronized Appointments.Appointment findAppointmentByPatientNameAndDate(String patientName, String date) throws SQLException {
        String query = "SELECT * FROM appointments WHERE name = ? AND appointment_date = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patientName);
            preparedStatement.setString(2, date);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String time = resultSet.getString("appointment_time");
                    String reason = resultSet.getString("appointment_reason");
                    return new Appointments.Appointment(patientName, date, time, reason);
                }
            }
        }
        return null;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}