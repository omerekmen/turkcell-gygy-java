package com.turkcell.repository;

import java.util.Stack;

import com.turkcell.model.CreditCard;

public class IMCreditCardRepository implements CreditCardRepository {
    private Stack<CreditCard> creditCardStack = new Stack<>();

    public String createCreditCard(String accountNumber, double userSalary) {
        CreditCard card = new CreditCard(accountNumber, userSalary);
        creditCardStack.push(card);
        return accountNumber;
    }

    public CreditCard[] getCreditCardsByAccountNumber(String accountNumber) {
        return creditCardStack.stream()
                .filter(card -> card.getAccountNumber().equals(accountNumber))
                .toArray(CreditCard[]::new);
    }

    public void removeCreditCard(String accountNumber, String cardNumber) {
        creditCardStack.removeIf(card -> card.getAccountNumber().equals(accountNumber) && card.getCardNumber().equals(cardNumber));
    }

    public void updateCreditCardBalance(String accountNumber, double newBalance) {
        // Implement logic to update the balance of a credit card
    }

}
