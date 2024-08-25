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

public class Patients extends Application {

    private PatientDBObject patientDBObject;
    private TextField patientName, patientAge;
    private TextArea patientMedicalHistory;
    private ListView<String> patientList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Patient Records");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");
        vbox.setAlignment(Pos.CENTER);

        patientDBObject = new PatientDBObject();
        patientName = new TextField();
        patientAge = new TextField();
        patientMedicalHistory = new TextArea();
        Button saveButton = new Button("Save/Update Patient");
        patientList = new ListView<>();

        saveButton.setOnAction(e -> updatePatientListView());

        vbox.getChildren().addAll(
                new Label("Patient Name:"), patientName,
                new Label("Age:"), patientAge,
                new Label("Medical History:"), patientMedicalHistory,
                saveButton,
                new Label("Patient List:"), patientList
        );

        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadPatients();

        primaryStage.setOnCloseRequest(e -> patientDBObject.shutdown());
    }

    private void loadPatients() {
        patientDBObject.executorService.submit(() -> {
            try {
                List<Patient> patients = patientDBObject.getAllPatients();
                Platform.runLater(() -> {
                    patientList.getItems().clear();
                    for (Patient patient : patients) {
                        patientList.getItems().add("Patient name : " +
                                patient.getName() + ", Age: " + patient.getAge() +
                                ", Medical History: " + patient.getMedicalHistory());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to load patient data"));
            }
        });
    }

    private void updatePatientListView() {
        String name = patientName.getText();
        String ageText = patientAge.getText();
        String history = patientMedicalHistory.getText();

        patientDBObject.executorService.submit(() -> {
            try {
                int age = Integer.parseInt(ageText);
                Patient patient = new Patient(name, age, history);
                patientDBObject.saveOrUpdatePatient(patient);
                Platform.runLater(() -> {
                    loadPatients();
                    clearFields();
                    showAlert("Success", "Patient saved successfully");
                });
            } catch (NumberFormatException e) {
                Platform.runLater(() -> showAlert("Error", "Invalid age format"));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to save patient data"));
            }
        });
    }

    private void clearFields() {
        patientName.clear();
        patientAge.clear();
        patientMedicalHistory.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class Patient {
        private String name;
        private int age;
        private String medicalHistory;

        public Patient(String name, int age, String medicalHistory) {
            this.name = name;
            this.age = age;
            this.medicalHistory = medicalHistory;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getMedicalHistory() {
            return medicalHistory;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}