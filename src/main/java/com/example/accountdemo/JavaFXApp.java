package com.example.accountdemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class JavaFXApp extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    private PasswordField newPasswordField;
    private Button resetPasswordButton;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        usernameField = new TextField();
        passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Login");


        registerButton.setOnAction(e -> handleRegistration());
        loginButton.setOnAction(e -> handleLogin());

        VBox vbox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, loginButton);
        Scene scene = new Scene(vbox, 300, 200);
        Label newPasswordLabel = new Label("New Password:");
        newPasswordField = new PasswordField();
        resetPasswordButton = new Button("Reset Password");

        resetPasswordButton.setOnAction(e -> handleResetPassword());

        vbox.getChildren().addAll(newPasswordLabel, newPasswordField, resetPasswordButton);
        primaryStage.setTitle("User Registration & Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = PasswordUtils.createSaltedHashedUser(username, password);
        if (user != null) {
            CSVUtils.saveUser(user);
            showInfoAlert("Registration Successful", "User registered successfully!");
        } else {
            showErrorAlert("Error", "An error occurred during registration.");
        }
    }

    private void handleResetPassword() {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();

        List<User> users = CSVUtils.loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                User updatedUser = PasswordUtils.createSaltedHashedUser(username, newPassword);
                if (updatedUser != null) {
                    // Update the user's password and save the changes to the CSV file.
                    user.setSalt(updatedUser.getSalt());
                    user.setPasswordHash(updatedUser.getPasswordHash());
                    CSVUtils.saveUsers(users);

                    showInfoAlert("Password Reset", "Password reset successfully!");
                    return;
                }
            }
        }

        showErrorAlert("Reset Password Failed", "Invalid username. Please try again.");
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        List<User> users = CSVUtils.loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && PasswordUtils.verifyPassword(user, password)) {
                showInfoAlert("Login Successful", "Welcome, " + username + "!");
                return;
            }
        }

        showErrorAlert("Login Failed", "Invalid username or password.");
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
