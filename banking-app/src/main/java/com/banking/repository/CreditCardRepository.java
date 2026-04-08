package com.banking.repository;

import com.banking.model.CreditCard;

public interface CreditCardRepository {
    String createCreditCard(String accountNumber, double userSalary);
    CreditCard[] getCreditCardsByAccountNumber(String accountNumber);
    void removeCreditCard(String accountNumber, String cardNumber);
    void updateCreditCardBalance(String accountNumber, double newBalance);   
}
