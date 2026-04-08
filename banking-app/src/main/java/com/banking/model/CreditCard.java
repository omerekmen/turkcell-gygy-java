package com.banking.model;

public class CreditCard extends Card {
    private double creditLimit;
    private double currentBalance;

    public CreditCard(String accountNumber, double userSalary) {
        super(accountNumber);
        this.creditLimit = userSalary * 3;
        this.currentBalance = 0.0;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

}
