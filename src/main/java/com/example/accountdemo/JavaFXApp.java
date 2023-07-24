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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.List;

import static com.example.accountdemo.CSVUtils.userExists;
import static com.example.accountdemo.UserPage.openUserPage;

public class JavaFXApp extends Application {
    private TextField usernameField;
    private PasswordField passwordField;

    private PasswordField newPasswordField;
    private Button resetPasswordButton;

    private ComboBox<String> userTypeComboBox;

    private TextField sentenceField;
    private Button loginButton;
    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        usernameField = new TextField();
        passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        sentenceField = new TextField();
        Button loginButton = new Button("Login");
        userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("RegularUser", "Admin");
        userTypeComboBox.setValue("RegularUser");


        registerButton.setOnAction(e -> handleRegistration());
        loginButton.setOnAction(e -> handleLogin());

        VBox vbox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, userTypeComboBox, registerButton, loginButton);
        Scene scene = new Scene(vbox, 300, 250);
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
        String userType = userTypeComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || userType.isEmpty()) {
            showErrorAlert("Registration Failed", "Please fill in all the fields.");
            return;
        }

        if (userExists(username)) {
            showErrorAlert("Registration Failed", "Username already exists.");
            return;
        }

        // Generate a salt and hash the password
        String salt = PasswordUtils.generateSalt();
        String passwordHash = PasswordUtils.hashPassword(password, salt);

        // Create a new user based on the selected user type
        User newUser;
        if (userType.equals("RegularUser")) {
            newUser = new RegularUser(username, salt, passwordHash, "");
        } else {
            newUser = new Admin(username, salt, passwordHash, "");
        }

        // Save the new user to the CSV file
        CSVUtils.registerUser(newUser);

        showInfoAlert("Registration Successful", "User registered successfully.");
        clearRegistrationFields();
    }



    private void clearRegistrationFields() {
        usernameField.clear();
        passwordField.clear();
        userTypeComboBox.getSelectionModel().clearSelection();
    }
    private void handleResetPassword() {
        String username = usernameField.getText();
        String newPassword = newPasswordField.getText();

        List<User> users = CSVUtils.loadUsers();
        User userToUpdate = null;

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                userToUpdate = user;
                break;
            }
        }

        if (userToUpdate == null) {
            showErrorAlert("Reset Password Failed", "Invalid username. Please try again.");
            return;
        }

        String userType = userToUpdate.getUserType();
        User updatedUser = PasswordUtils.createSaltedHashedUser(username, newPassword, userType);
        if (updatedUser != null) {
            userToUpdate.setSalt(updatedUser.getSalt());
            userToUpdate.setPasswordHash(updatedUser.getPasswordHash());
            CSVUtils.saveUsers(users);

            showInfoAlert("Password Reset", "Password reset successfully!");
        } else {
            showErrorAlert("Error", "An error occurred during password reset.");
        }
    }


    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String userType = userTypeComboBox.getValue(); // Get the selected user type from the ComboBox

        if (!CSVUtils.userExists(username)) {
            showErrorAlert("Login Failed", "Username does not exist.");
            return;
        }

        User loggedInUser = CSVUtils.loadUser(username);

        if (!PasswordUtils.verifyPassword(loggedInUser, password)) {
            showErrorAlert("Login Failed", "Invalid password.");
            return;
        }

        if (!loggedInUser.getUserType().equals(userType)) {
            showErrorAlert("Login Failed", "Invalid credentials.");
            return;
        }

        // Open the UserPage for the logged-in user
        primaryStage.close();
        UserPage.openUserPage(loggedInUser);

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
