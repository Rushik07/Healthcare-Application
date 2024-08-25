package com.example.projectwork;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setVgap(10);
        grid.setHgap(10);
        
        Label heading = new Label("Healthcare Application Login");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 15)); 
        GridPane.setConstraints(heading, 0, 0, 2, 1);
        grid.setAlignment(Pos.CENTER); 
        grid.getChildren().add(heading);

        // Add styles
        grid.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");

        
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        grid.add(errorMessage, 1, 7);

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> primaryStage.close());
        grid.add(exitBtn, 1, 4);

        loginButton.setOnAction(e -> {
            if (usernameField.getText().matches("\\w{6,12}") && passwordField.getText().matches("\\w{8,}")) {
                dashboardPage();
            } else {
                errorMessage.setText("Invalid username or password.");
                showAlert("Login Failed", "Please check your credentials and try login again.");
            }
        });

        Scene scene = new Scene(grid, 300, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void dashboardPage() {
        MainDashboard dashboard = new MainDashboard();
        dashboard.start(new Stage());
        primaryStage.close();
    }
//    private boolean loginValidation(String username, String password) {
//        return "admin".equals(username) && "password".equals(password);
//    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
