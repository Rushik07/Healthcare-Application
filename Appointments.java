package com.example.projectwork;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class Appointments extends Application {
    private AppointmentDBObject appointmentDBObject;
    private TextField nameField, dateField, timeField;
    private TextArea reasonField;
    private ListView<String> appointmentList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Appointments");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");
        nameField = new TextField();
        dateField = new TextField();
        timeField = new TextField();
        reasonField = new TextArea();
        Button save = new Button("Save or Update Appointment");
        appointmentList = new ListView<>();

        save.setOnAction(e -> saveOrUpdateAppointment());

        vbox.getChildren().addAll(
                new Label("Patient Name:"), nameField,
                new Label("Date:"), dateField,
                new Label("Time:"), timeField,
                new Label("Reason for appointment:"), reasonField, save,
                new Label("Appointments"), appointmentList
        );

        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        appointmentDBObject = new AppointmentDBObject();
        loadAppointments();

        primaryStage.setOnCloseRequest(e -> appointmentDBObject.shutdown());
    }

    private void saveOrUpdateAppointment() {
        String patientName = nameField.getText();
        String date = dateField.getText();
        String time = timeField.getText();
        String reason = reasonField.getText();

        appointmentDBObject.executorService.submit(() -> {
            try {
                Appointments.Appointment existingAppointment = appointmentDBObject.findAppointmentByPatientNameAndDate(patientName, date);
                Appointments.Appointment appointment = new Appointments.Appointment(patientName, date, time, reason);
                appointmentDBObject.saveOrUpdateAppointment(appointment);
                Platform.runLater(() -> {
                    showAlert("Success", existingAppointment != null ? "Appointment updated successfully" : "Appointment scheduled successfully");
                    loadAppointments();
                    clearFields();
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to save appointment data"));
            }
        });
    }

    private void loadAppointments() {
        appointmentDBObject.executorService.submit(() -> {
            try {
                List<Appointments.Appointment> appointments = appointmentDBObject.getAllAppointments();
                Platform.runLater(() -> {
                    appointmentList.getItems().clear();
                    for (Appointments.Appointment appointment : appointments) {
                        appointmentList.getItems().add(appointment.toString());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to load appointment data"));
            }
        });
    }

    private void clearFields() {
        nameField.clear();
        dateField.clear();
        timeField.clear();
        reasonField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class Appointment {
        private String patientName;
        private String date;
        private String time;
        private String reason;

        public Appointment(String patientName, String date, String time, String reason) {
            this.patientName = patientName;
            this.date = date;
            this.time = time;
            this.reason = reason;
        }

        public String getPatientName() {
            return patientName;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getReason() {
            return reason;
        }

        @Override
        public String toString() {
            return String.format("%s - %s %s: %s", patientName, date, time, reason);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}