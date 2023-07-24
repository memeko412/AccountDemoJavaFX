package com.example.accountdemo;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserPage {
    public static void openUserPage(User loggedInUser) {
        Stage stage = new Stage();

        Label usernameLabel = new Label("Username: " + loggedInUser.getUsername());
        Label userTypeLabel = new Label("User Type: " + loggedInUser.getUserType());
        Label sentenceLabel = new Label("Enter a Sentence:");
        TextField sentenceField = new TextField();
        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {
            String sentence = sentenceField.getText();
            loggedInUser.setSentence(sentence);
            CSVUtils.saveUser(loggedInUser); // Update the logged-in user's information in the CSV file.
            sentenceField.clear();
        });

        VBox vbox = new VBox(10, usernameLabel, userTypeLabel, sentenceLabel, sentenceField, saveButton);
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.setTitle("User Page");
        stage.show();

        // Show a popup with the user's sentence (if available)
        if (!loggedInUser.getSentence().isEmpty()) {
            Alert sentencePopup = new Alert(Alert.AlertType.INFORMATION);
            sentencePopup.setHeaderText("Your Sentence");
            sentencePopup.setContentText(loggedInUser.getSentence());
            sentencePopup.showAndWait();
        }
    }
}
