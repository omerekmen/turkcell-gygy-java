package com.banking.model;

public class BankCard extends Card {
    private double balance;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawalAmount;

    public BankCard(String accountNumber, double balance) {
        super(accountNumber);
        this.balance = balance;
        this.dailyWithdrawalLimit = 1000.0;
        this.dailyWithdrawalAmount = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public double getDailyWithdrawalAmount() {
        return dailyWithdrawalAmount;
    }

    public void setDailyWithdrawalAmount(double dailyWithdrawalAmount) {
        this.dailyWithdrawalAmount = dailyWithdrawalAmount;
    }

    public boolean canWithdraw(double amount) {
        return (dailyWithdrawalAmount + amount <= dailyWithdrawalLimit) && (balance >= amount);
    }

    public void resetDailyWithdrawalAmount() {
        this.dailyWithdrawalAmount = 0.0;
    }
}
