package com.turkcell.repository;

import java.util.Stack;

import com.turkcell.model.BankCard;

public class IMBankCardRepository implements BankCardRepository {
    private Stack<BankCard> bankCardStack = new Stack<>();

    public String createBankCard(String accountNumber) {
        BankCard card = new BankCard(accountNumber, 0);
        bankCardStack.push(card);
        return card.getCardNumber();
    }

    public BankCard[] getBankCardsByAccountNumber(String accountNumber) {
        return bankCardStack.stream()
                .filter(card -> card.getAccountNumber().equals(accountNumber))
                .toArray(BankCard[]::new);
    }

    public BankCard getBankCardByAccountNumber(String accountNumber) {
        for (BankCard card : bankCardStack) {
            if (card.getAccountNumber().equals(accountNumber)) {
                return card;
            }
        }
        return null;
    }
    
    public void removeBankCard(String accountNumber, String cardNumber) {
        bankCardStack.removeIf(card -> card.getAccountNumber().equals(accountNumber) && card.getCardNumber().equals(cardNumber));
    }

    public void updateBankCardBalance(String accountNumber, double newBalance) {
        for (BankCard card : bankCardStack) {
            if (card.getAccountNumber().equals(accountNumber)) {
                card.setBalance(newBalance);
                break;
            }
        }
    }

}
