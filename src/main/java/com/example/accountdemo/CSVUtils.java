package com.example.accountdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    private static final String CSV_FILE = "accounts.csv";
    private static final String CSV_DELIMITER = ",";


    public static void registerUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(user.getUserType() + CSV_DELIMITER + user.getUsername() + CSV_DELIMITER
                    + user.getSalt() + CSV_DELIMITER + user.getPasswordHash() + CSV_DELIMITER + "\"" + user.getSentence() + "\"");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_DELIMITER);
                if (data.length >= 2 && data[1].equals(username)) {
                    return true; // User with the specified username exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User does not exist
    }


    public static void saveUser(User user) {
        List<User> users = loadUsers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (User u : users) {
                if (u.getUsername().equals(user.getUsername())) {
                    // Update the existing user's information
                    u.setSalt(user.getSalt());
                    u.setPasswordHash(user.getPasswordHash());
                    u.setSentence(user.getSentence());
                }
                writer.write(u.getUserType() + CSV_DELIMITER + u.getUsername() + CSV_DELIMITER
                        + u.getSalt() + CSV_DELIMITER + u.getPasswordHash() + CSV_DELIMITER + "\"" + u.getSentence() + "\"");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static User loadUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_DELIMITER);
                if (data.length >= 5 && data[1].equals(username)) {
                    String userType = data[0];
                    String salt = data[2];
                    String passwordHash = data[3];
                    String sentence = data[4].replace("\"", ""); // Remove the quotes around the sentence

                    if (userType.equals("RegularUser")) {
                        return new RegularUser(username, salt, passwordHash, sentence);
                    } else if (userType.equals("Admin")) {
                        return new Admin(username, salt, passwordHash, sentence);
                    } else {
                        throw new IllegalArgumentException("Invalid user type.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // User with the specified username not found
    }
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_DELIMITER);
                if (data.length >= 5) {
                    String userType = data[0];
                    String username = data[1];
                    String salt = data[2];
                    String passwordHash = data[3];
                    String sentence = data[4].replace("\"", ""); // Remove the quotes around the sentence

                    if (userType.equals("RegularUser")) {
                        users.add(new RegularUser(username, salt, passwordHash, sentence));
                    } else if (userType.equals("Admin")) {
                        users.add(new Admin(username, salt, passwordHash, sentence));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }



    public static void saveUsers(List<User> usersList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (User user : usersList) {
                writer.write(user.getUserType() + CSV_DELIMITER + user.getUsername() + CSV_DELIMITER
                        + user.getSalt() + CSV_DELIMITER + user.getPasswordHash() + CSV_DELIMITER + "\"" + user.getSentence() + "\"");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
