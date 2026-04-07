package com.banking.repository;

import com.banking.model.BankAccount;

public interface BankAccountRepository {
    String createAccount(String tckn, double initialBalance);
    BankAccount getAccount(String accountNumber);
    BankAccount getAccountByTckn(String tckn);
    void updateBalance(String accountNumber, double newBalance);
}
