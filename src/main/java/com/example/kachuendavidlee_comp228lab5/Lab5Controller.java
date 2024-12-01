package com.example.kachuendavidlee_comp228lab5;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;


public class Lab5Controller {

    // Player Information fields
    @FXML private TextField playerIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField postalCodeField;
    @FXML private TextField provinceField;
    @FXML private TextField phoneNumberField;

    // Game Information fields
    @FXML private TextField gameIdField;
    @FXML private TextField gameTitleField;

    // Buttons
    @FXML private Button insertPlayerButton;
    @FXML private Button updatePlayerButton;
    @FXML private Button insertGameButton;

    // Button event handlers
    @FXML
    private void handleInsertPlayer() {
        String playerId = playerIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String province = provinceField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Add logic to insert player into database or data structure
        System.out.println("Inserting Player: " + firstName + " " + lastName);
    }

    @FXML
    private void handleUpdatePlayer() {
        String playerId = playerIdField.getText();

        // Add logic to update player information
        System.out.println("Updating Player ID: " + playerId);
    }

    @FXML
    private void handleInsertGame() {
        String gameId = gameIdField.getText();
        String gameTitle = gameTitleField.getText();

        // Add logic to insert game into database or data structure
        System.out.println("Inserting Game: " + gameTitle);
    }
}

