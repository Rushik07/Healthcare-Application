package com.example.projectwork;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainDashboard extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dashboard");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
  // Add style
        vbox.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");
        vbox.setAlignment(Pos.CENTER);
        
        Label heading = new Label("Welcome to Dashboard");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        Button patientButton = new Button("Patients");
        Button appointmentButton = new Button("Appointments");
        Button logoutButton = new Button("Logout");
        Button exitBtn = new Button("Exit");

        exitBtn.setOnAction(e -> primaryStage.close());
        patientButton.setOnAction(e -> {
        	Patients patientRecords = new Patients();
        patientRecords.start(new Stage());
        });
        appointmentButton.setOnAction(e -> {
        	Appointments appointments = new Appointments();
        appointments.start(new Stage());
        });
        logoutButton.setOnAction(e -> {
            Login loginScreen = new Login();
        loginScreen.start(new Stage());
        primaryStage.close();
        });

        vbox.getChildren().addAll(heading,patientButton, appointmentButton, logoutButton,exitBtn);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   
}
