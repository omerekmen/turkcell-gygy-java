package com.banking.repository;

import com.banking.model.User;

import java.util.Stack;

public class IMUserRepository implements UserRepository { // InMemoryUserRepository
    private final Stack<User> userStack = new Stack<>();

    public String addUser(String name, String tckn, String password) {
        if (name == null || tckn == null || password == null) {
            throw new IllegalArgumentException("name, tckn and password cannot be null");
        }

        User user = new User(name, tckn, password);
        userStack.push(user); // keep user in in-memory stack
        return user.getTckn(); // Return TCKN as a confirmation of user creation
    }

    public User getUser(String tckn, String password) {
        // In-memory implementation to retrieve a user
        for (User user : userStack) {
            if (user.getTckn().equals(tckn) && user.getPassword().equals(password)) {
                return user; // Return the user if found
            }
        }
        return null; // Placeholder return statement
    }

    public boolean checkUserExists(String tckn) {
        for (User user : userStack) {
            if (user.getTckn().equals(tckn)) {
                return true; // Return true if user is found
            }
        }
        return false; // Return false if user is not found
    }

    public boolean checkTcknRules(String tckn) {
        return tckn != null && tckn.matches("\\d{11}"); // Example: TCKN must be exactly 11 digits
    }
}