package com.example.accountdemo;

public class Admin extends User {

    public Admin(String username, String salt, String passwordHash, String sentence) {
        super(username, salt, passwordHash);
        this.setSentence(sentence);
    }

    @Override
    public String getUserType() {
        return "Admin";
    }

    // Add any specific methods or attributes for Admin if needed.
}
