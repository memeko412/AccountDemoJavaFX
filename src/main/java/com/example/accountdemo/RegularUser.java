package com.example.accountdemo;

public class RegularUser extends User {

    public RegularUser(String username, String salt, String passwordHash, String sentence) {
        super(username, salt, passwordHash);
        this.setSentence(sentence);
    }

    @Override
    public String getUserType() {
        return "RegularUser";
    }

    // Add any specific methods or attributes for RegularUser if needed.
}

