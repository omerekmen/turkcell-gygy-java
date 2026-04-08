package com.banking.repository;

import com.banking.model.BankCard;

public interface BankCardRepository {
    String createBankCard(String accountNumber);
    BankCard[] getBankCardsByAccountNumber(String accountNumber);
    BankCard getBankCardByAccountNumber(String accountNumber);
    void removeBankCard(String accountNumber, String cardNumber);
    void updateBankCardBalance(String accountNumber, double newBalance);
}
