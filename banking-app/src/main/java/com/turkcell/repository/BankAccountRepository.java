package com.turkcell.repository;

import com.turkcell.model.BankAccount;

public interface BankAccountRepository {
    String createAccount(String tckn, double initialBalance);
    BankAccount getAccount(String accountNumber);
    BankAccount getAccountByTckn(String tckn);
    void updateBalance(String accountNumber, double newBalance);
}
