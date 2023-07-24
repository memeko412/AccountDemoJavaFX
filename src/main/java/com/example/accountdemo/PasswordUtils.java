package com.example.accountdemo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {
    private static final int SALT_LENGTH = 16; // Length of the salt in bytes
    public static User createSaltedHashedUser(String username, String password, String userType) {
        String salt = PasswordUtils.generateSalt();
        String passwordHash = PasswordUtils.hashPassword(password, salt);

        if (userType.equals("RegularUser")) {
            return new RegularUser(username, salt, passwordHash, "");
        } else if (userType.equals("Admin")) {
            return new Admin(username, salt, passwordHash, "");
        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }
    }

    // Add a new method for password reset, considering user type
    public static User resetPassword(String username, String newPassword, String userType) {
        return createSaltedHashedUser(username, newPassword, userType);
    }


    public static boolean verifyPassword(User user, String password) {
        String salt = user.getSalt();
        String hashedPassword = user.getPasswordHash();
        String hashedEnteredPassword = hashPassword(password, salt);
        return hashedEnteredPassword.equals(hashedPassword);
    }


    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hashPassword(String password, String salt) {
        String saltedPassword = salt + password;
        String hashedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            hashedPassword = bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
