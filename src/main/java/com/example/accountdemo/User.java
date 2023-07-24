package com.example.accountdemo;

import java.util.Base64;
import java.util.Random;

public abstract class User {
    private String username;
    private String salt;
    private String passwordHash;
    private String sentence;

    public User(String username, String salt, String passwordHash) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.sentence = "";
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Add an abstract method to be implemented by subclasses.
    public abstract String getUserType();

    // Other methods like setters, etc. if needed.
}
