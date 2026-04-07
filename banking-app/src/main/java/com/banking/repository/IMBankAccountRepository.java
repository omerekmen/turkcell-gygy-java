package com.banking.repository;

import com.banking.model.BankAccount;

import java.util.Stack;

public class IMBankAccountRepository implements BankAccountRepository {
    private Stack<BankAccount> accounts = new Stack<>();

    public String createAccount(String tckn, double initialBalance) {
        BankAccount account = new BankAccount(tckn, initialBalance);
        accounts.push(account);
        return account.getAccountNumber();
    }

    public BankAccount getAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null; // Return null if account not found
    }

    public BankAccount getAccountByTckn(String tckn) {
        for (BankAccount account : accounts) {
            if (account.getTckn().equals(tckn)) {
                return account;
            }
        }
        return null; // Return null if account not found
    }
    
    public void updateBalance(String accountNumber, double newBalance) {
        BankAccount account = getAccount(accountNumber);
        if (account != null) {
            account.setBalance(newBalance);
        }
    }

}
