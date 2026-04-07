package com.banking.model;

public class BankAccount {
    private static long accountSequence = 1000000000L;

    private String tckn; // Added TCKN to link account to user
    private String accountNumber;
    private double balance;

    public BankAccount(String tckn, double balance) {
        this.accountNumber = generateAccountNumber();
        this.tckn = tckn;
        this.balance = balance;
    }

    public String getTckn() {
        return tckn;
    }

    private static synchronized String generateAccountNumber() {
        return String.valueOf(accountSequence++);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
