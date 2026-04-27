package com.turkcell.model;

public class Card {
    private static long cardSequence = 4455000099880000L; // Starting point for card numbers

    private String accountNumber;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public Card(String accountNumber) {
        this.accountNumber = accountNumber;
        this.cardNumber = generateCardNumber();
        this.expiryDate = setExpiryDate();
        this.cvv = generateCvv();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String displayCardNumber() {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private static synchronized String generateCardNumber() {
        return String.valueOf(cardSequence++);
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    private static String setExpiryDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate expiry = today.plusYears(3);
        return String.format("%02d/%02d", expiry.getMonthValue(), expiry.getYear() % 100);
    }

    public String getCvv() {
        return cvv;
    }

    private static String generateCvv() {
        return String.format("%03d", (int) (Math.random() * 1000));
    }

    public static boolean validateCardNumber(String cardNumber) {
        // Simple validation: check if it's 16 digits
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }
}
