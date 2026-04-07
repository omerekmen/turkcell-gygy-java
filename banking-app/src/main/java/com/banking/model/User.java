package com.banking.model;

public class User {
    private String name;
    private String tckn;
    private String password; // In a real application, passwords should be hashed and salted

    public User(String name, String tckn, String password) {
        this.setName(name);
        this.setTckn(tckn);
        this.setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        if (tckn == null || !tckn.matches("\\d{11}")) {
            throw new IllegalArgumentException("TCKN must be exactly 11 digits");
        }
        this.tckn = tckn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        this.password = password;
    }
}
