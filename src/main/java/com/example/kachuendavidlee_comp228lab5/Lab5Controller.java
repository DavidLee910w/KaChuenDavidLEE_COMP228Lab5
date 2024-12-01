package com.example.kachuendavidlee_comp228lab5;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.sql.*;

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
    @FXML private TextField Score;
    @FXML private DatePicker PlayingDate;
    @FXML private TextField playerIdInputField;

    // Query fields
    @FXML private TextField playerIdQueryField;
    @FXML private TextArea resultTextArea;

    // Buttons
    @FXML private Button insertPlayerButton;
    @FXML private Button updatePlayerButton;
    @FXML private Button insertGameButton;
    @FXML private Button enquirePlayerRating;

    // Database credentials and URL
    private static final String DB_URL = "jdbc:oracle:thin:@199.212.26.208:1521/SQLD";
    private static final String DB_USER = "COMP228_F24_soh_16";
    private static final String DB_PASSWORD = "Devi1027";

    // Establish a connection to the Oracle database
    private Connection connectToDatabase() {
        Connection connection = null;
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");

            // Connect to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to Oracle Database successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the Oracle Database.");
            e.printStackTrace();
        }
        return connection;
    }

    // Example: Insert a player into the database
    private void insertPlayerIntoDatabase(Integer playerId, String firstName, String lastName, String address,
                                          String postalCode, String province, Integer phoneNumber) {
        String insertQuery = "INSERT INTO Player (Player_ID, First_Name, Last_Name, Address, Postal_Code, Province, Phone_Number) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, postalCode);
            preparedStatement.setString(6, province);
            preparedStatement.setInt(7, phoneNumber);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new player was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Call this method when the "Insert Player" button is clicked
    @FXML
    private void handleInsertPlayer() {
        try {
            Integer playerId = Integer.parseInt(playerIdField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String province = provinceField.getText();
            Integer phoneNumber = Integer.parseInt(phoneNumberField.getText()); // Use Long.parseLong here

            System.out.println("Inserting Player: " + firstName + " " + lastName);
            insertPlayerIntoDatabase(playerId, firstName, lastName, address, postalCode, province, phoneNumber);
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
    // Insert a game into the database
    private void insertGameIntoDatabase(Integer gameId, String gameTitle) {
        String insertQuery = "INSERT INTO Game (GAME_ID, GAME_TITLE) VALUES (?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, gameId);
            preparedStatement.setString(2, gameTitle);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new game was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Call this method when the "Insert Game" button is clicked
    @FXML
    private void handleInsertGame() {
        try {
            Integer gameId = Integer.parseInt(gameIdField.getText());
            String gameTitle = gameTitleField.getText();
            Integer playerId = Integer.parseInt(playerIdInputField.getText());
            Double score = Double.parseDouble(Score.getText());
            String playingDate = PlayingDate.getValue().toString(); // Convert LocalDate to String

            // First insert the game
            insertGameIntoDatabase(gameId, gameTitle);

            int playerGameId = gameId + (playerId*1000);

            // Insert the player-game relationship
            insertPlayerGameIntoDatabase(playerGameId, playerId, gameId, playingDate, score);

            // Clear the fields after successful insertion
            gameIdField.clear();
            gameTitleField.clear();
            playerIdInputField.clear();
            Score.clear();
            PlayingDate.setValue(null);

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Please fill in all fields: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inserting game and relationship: " + e.getMessage());
        }
    }
    // Insert a player-game relationship into the Playerandgame table
    private void insertPlayerGameIntoDatabase(Integer playerGameId, Integer playerId, Integer gameId, String playingDate, Double score) {
        String insertQuery = "INSERT INTO Playerandgame (PLAYER_GAME_ID, PLAYER_ID, GAME_ID, PLAYING_DATE, SCORE) "
                + "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, playerGameId);
            preparedStatement.setInt(2, playerId);
            preparedStatement.setInt(3, gameId);
            preparedStatement.setString(4, playingDate);
            preparedStatement.setDouble(5, score);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new player-game record was inserted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting player-game relationship: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleUpdatePlayer() {
        try {
            Integer playerId = Integer.parseInt(playerIdField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String province = provinceField.getText();
            Long phoneNumber = Long.parseLong(phoneNumberField.getText());

            String updateQuery = "UPDATE Player SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, POSTAL_CODE = ?, PROVINCE = ?, PHONE_NUMBER = ? WHERE PLAYER_ID = ?";

            try (Connection connection = connectToDatabase();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, address);
                preparedStatement.setString(4, postalCode);
                preparedStatement.setString(5, province);
                preparedStatement.setLong(6, phoneNumber);
                preparedStatement.setInt(7, playerId);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Player information updated successfully!");
                } else {
                    System.out.println("No player found with the given ID.");
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.err.println("Error updating player: " + e.getMessage());
        }
    }
    @FXML
    private void enquirePlayerRating() {
        try {
            Integer playerId = Integer.parseInt(playerIdQueryField.getText());
            String query = "SELECT p.FIRST_NAME, p.LAST_NAME, g.GAME_TITLE, pg.PLAYING_DATE, pg.SCORE " +
                    "FROM Player p " +
                    "JOIN Playerandgame pg ON p.PLAYER_ID = pg.PLAYER_ID " +
                    "JOIN Game g ON g.GAME_ID = pg.GAME_ID " +
                    "WHERE p.PLAYER_ID = ? " +
                    "ORDER BY pg.PLAYING_DATE";

            try (Connection connection = connectToDatabase();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, playerId);
                ResultSet resultSet = preparedStatement.executeQuery();

                StringBuilder content = new StringBuilder();
                boolean hasRecords = false;

                while (resultSet.next()) {
                    hasRecords = true;
                    content.append(String.format("Name: %s %s\n",
                                    resultSet.getString("FIRST_NAME"),
                                    resultSet.getString("LAST_NAME")))
                            .append(String.format("Game: %s\n", resultSet.getString("GAME_TITLE")))
                            .append(String.format("Date: %s\n", resultSet.getDate("PLAYING_DATE")))
                            .append(String.format("Score: %.1f\n", resultSet.getDouble("SCORE")))
                            .append("------------------------\n");
                }

                if (!hasRecords) {
                    resultTextArea.setText("No game records found for Player ID: " + playerId);
                } else {
                    resultTextArea.setText(content.toString());
                }

            } catch (SQLException e) {
                resultTextArea.setText("Database Error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            resultTextArea.setText("Error: Please enter a valid numeric Player ID");
        }
    }
//    @FXML
//    private void displayPlayerGameReport() {
//        String query = "SELECT p.PLAYER_ID, p.FIRST_NAME, p.LAST_NAME, g.GAME_TITLE, pg.PLAYING_DATE, pg.SCORE "
//                + "FROM Player p "
//                + "JOIN Playerandgame pg ON p.PLAYER_ID = pg.PLAYER_ID "
//                + "JOIN Game g ON g.GAME_ID = pg.GAME_ID";
//
//        try (Connection connection = connectToDatabase();
//             PreparedStatement preparedStatement = connection.prepareStatement(query);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            System.out.println("Player Game Report:");
//            while (resultSet.next()) {
//                System.out.printf("Player: %s %s, Game: %s, Date: %s, Score: %.1f%n",
//                        resultSet.getString("FIRST_NAME"),
//                        resultSet.getString("LAST_NAME"),
//                        resultSet.getString("GAME_TITLE"),
//                        resultSet.getDate("PLAYING_DATE"),
//                        resultSet.getDouble("SCORE"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}



