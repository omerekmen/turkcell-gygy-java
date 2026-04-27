package com.turkcell;

import java.util.Scanner;

import com.turkcell.model.BankAccount;
import com.turkcell.model.BankCard;
import com.turkcell.model.CreditCard;
import com.turkcell.model.User;
import com.turkcell.repository.BankAccountRepository;
import com.turkcell.repository.BankCardRepository;
import com.turkcell.repository.CreditCardRepository;
import com.turkcell.repository.UserRepository;

public class Operations {
    Scanner scanner;
    User loggedInUser;
    BankAccount userAccount;
    UserRepository userRepository;
    BankAccountRepository bankAccountRepository;
    CreditCardRepository creditCardRepository;
    BankCardRepository bankCardRepository;

    public Operations(
        Scanner scanner, 
        User loggedInUser, 
        UserRepository userRepository, 
        BankAccountRepository bankAccountRepository, 
        CreditCardRepository creditCardRepository, 
        BankCardRepository bankCardRepository
    ) {
        this.scanner = scanner;
        this.loggedInUser = loggedInUser;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.creditCardRepository = creditCardRepository;
        this.bankCardRepository = bankCardRepository;
        this.userAccount = bankAccountRepository.getAccountByTckn(loggedInUser.getTckn());
    }

    public void mainMenu() {
        while (loggedInUser != null) {
            System.out.println("\n------------------------------------------------------------------------");
            System.out.println("\nWelcome to your banking dashboard, " + loggedInUser.getName() + "!");
            
            System.out.println("Please select an option:");
            System.out.println("1. View account details");
            System.out.println("2. Make a transaction");
            System.out.println("3. Card Menu");
            System.out.println("4. Log out");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\n------------------------------------------------------------------------");
                    System.out.println("Viewing account details...");
                    System.out.println("------------------------------------------------------------------------ \n");
                    
                    getAccountDetails();

                    System.out.println("------------------------------------------------------------------------ \n");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                    continue;
                case "2":
                    System.out.println("\n------------------------------------------------------------------------");
                    System.out.println("Making a transaction...");
                    System.out.println("------------------------------------------------------------------------ \n");

                    transactionMenu();

                    System.out.println("------------------------------------------------------------------------ \n");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                    continue;
                case "3":
                    System.out.println("\n------------------------------------------------------------------------");
                    System.out.println("Viewing card details...");
                    System.out.println("------------------------------------------------------------------------ \n");

                    cardMenu();

                    System.out.println("------------------------------------------------------------------------ \n");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                    continue;
                case "4":
                    System.out.println("Logging out...");
                    loggedInUser = null;
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue;
            }
        }  
    }

    public void getAccountDetails() {
        if (userAccount != null) {
            System.out.println("Name: " + loggedInUser.getName());
            System.out.println("TCKN: " + loggedInUser.getTckn());
            System.out.println("Account Number: " + userAccount.getAccountNumber());
            System.out.println("Balance: " + userAccount.getBalance());
        } else {
            System.out.println("No bank account found for this user.");
        }
    }

    public void transactionMenu() {
        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Please select a transaction type:");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Return to main menu");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Depositing money...");
                System.out.println("------------------------------------------------------------------------ \n");

                System.out.println("Please enter the amount to deposit:");
                String amountStr = scanner.nextLine();
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than zero. Please try again.");
                        transactionMenu();
                        return;
                    }
                    double newBalance = userAccount.getBalance() + amount;
                    bankAccountRepository.updateBalance(userAccount.getAccountNumber(), newBalance);
                    System.out.println("Deposit successful! New balance: " + newBalance);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount format. Please enter a valid number.");
                    transactionMenu();
                    return;
                }

                System.out.println("------------------------------------------------------------------------ \n");
                System.out.println("Press Enter to return to the transaction menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                transactionMenu();
                break;
            case "2":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Withdrawing money...");
                System.out.println("------------------------------------------------------------------------ \n");

                System.out.println("Please enter the amount to withdraw:");
                String withdrawAmountStr = scanner.nextLine();
                try {
                    double amount = Double.parseDouble(withdrawAmountStr);
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than zero. Please try again.");
                        transactionMenu();
                        return;
                    }
                    if (amount > userAccount.getBalance()) {
                        System.out.println("Insufficient funds. Your current balance is: " + userAccount.getBalance());
                        transactionMenu();
                        return;
                    }
                    double newBalance = userAccount.getBalance() - amount;
                    bankAccountRepository.updateBalance(userAccount.getAccountNumber(), newBalance);
                    System.out.println("Withdrawal successful! New balance: " + newBalance);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount format. Please enter a valid number.");
                    transactionMenu();
                    return;
                }

                System.out.println("------------------------------------------------------------------------ \n");
                System.out.println("Press Enter to return to the transaction menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                transactionMenu();
                break;
            case "3":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Transferring money...");
                System.out.println("------------------------------------------------------------------------ \n");

                System.out.println("Please enter the recipient's account number:");
                String recipientAccountNumber = scanner.nextLine();
                BankAccount recipientAccount = bankAccountRepository.getAccount(recipientAccountNumber);
                if (recipientAccount == null) {
                    System.out.println("Recipient account not found. Please try again.");
                    transactionMenu();
                    return;
                } else if (recipientAccount.getAccountNumber().equals(userAccount.getAccountNumber())) {
                    System.out.println("You cannot transfer money to your own account. Please try again.");
                    transactionMenu();
                    return;
                }
                System.out.println("Please enter the amount to transfer:");
                String transferAmountStr = scanner.nextLine();
                try {
                    double amount = Double.parseDouble(transferAmountStr);
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than zero. Please try again.");
                        transactionMenu();
                        return;
                    }
                    if (amount > userAccount.getBalance()) {
                        System.out.println("Insufficient funds. Your current balance is: " + userAccount.getBalance());
                        transactionMenu();
                        return;
                    }
                    double newSenderBalance = userAccount.getBalance() - amount;
                    double newRecipientBalance = recipientAccount.getBalance() + amount;
                    bankAccountRepository.updateBalance(userAccount.getAccountNumber(), newSenderBalance);
                    bankAccountRepository.updateBalance(recipientAccount.getAccountNumber(), newRecipientBalance);
                    System.out.println("Transfer successful! Your new balance: " + newSenderBalance);
                    System.out.println("Recipient's new balance: " + newRecipientBalance + " (Account Number: " + recipientAccount.getAccountNumber() + ")" + " (For test purposes)");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount format. Please enter a valid number.");
                    transactionMenu();
                    return;
                }

                System.out.println("------------------------------------------------------------------------ \n");
                System.out.println("Press Enter to return to the transaction menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                transactionMenu();
                break;
            case "4":
                mainMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                transactionMenu();
                break;
        }
    }

    public void cardMenu() {
        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Select Operation:");
        System.out.println("1. View Bank Cards");
        System.out.println("2. View Credit Cards");
        System.out.println("3. Return to Main Menu");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Your Bank Cards:");
                System.out.println("------------------------------------------------------------------------ \n");
                BankCard[] bankCards = bankCardRepository.getBankCardsByAccountNumber(userAccount.getAccountNumber());
                if (bankCards.length == 0) {
                    System.out.println("You have no bank cards associated with this account.");
                } else {
                    for (BankCard card : bankCards) {
                        System.out.println("Card Number: " + card.displayCardNumber());
                        System.out.println("Balance: " + card.getBalance());
                        System.out.println("Daily Withdrawal Limit: " + card.getDailyWithdrawalLimit());
                        System.out.println("Daily Withdrawal Amount: " + card.getDailyWithdrawalAmount());
                        System.out.println("------------------------------------------------------------------------");
                    }
                }
                System.out.println("You can press 1 to create a new bank card for this account or press Enter to return to the card menu.");
                String createCardChoice = scanner.nextLine();
                if (createCardChoice.equals("1")) {
                    String newCardNumber = bankCardRepository.createBankCard(userAccount.getAccountNumber());
                    System.out.println("New bank card created successfully! Card Number: " + newCardNumber);
                }
                System.out.println("Press Enter to return to the card menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                cardMenu();
                break;  
            case "2":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Your Credit Cards:");
                System.out.println("------------------------------------------------------------------------ \n");
                CreditCard[] creditCards = creditCardRepository.getCreditCardsByAccountNumber(userAccount.getAccountNumber());
                if (creditCards.length == 0) {
                    System.out.println("You have no credit cards associated with this account.");
                } else {
                    for (CreditCard card : creditCards) {
                        System.out.println("Card Number: " + card.displayCardNumber());
                        System.out.println("Credit Limit: " + card.getCreditLimit());
                        System.out.println("Current Balance: " + card.getCurrentBalance());
                        System.out.println("------------------------------------------------------------------------");
                    }
                }
                System.out.println("You can press 1 to create a new credit card for this account or press Enter to return to the card menu.");
                String createCreditCardChoice = scanner.nextLine();
                if (createCreditCardChoice.equals("1")) {
                    System.out.println("Please enter your monthly salary to determine your credit limit:");
                    String userSalary = scanner.nextLine();
                    try {
                        double salary = Double.parseDouble(userSalary);
                        if (salary <= 0) {
                            System.out.println("Salary must be greater than zero. Please try again.");
                            cardMenu();
                            return;
                        } else if (salary < 1000) {
                            System.out.println("Minimum salary requirement for credit card is 1000. Please try again.");
                            cardMenu();
                            return;
                        }
                        String newCreditCardNumber = creditCardRepository.createCreditCard(userAccount.getAccountNumber(), salary);
                        System.out.println("New credit card created successfully! Card Number: " + newCreditCardNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid salary format. Please enter a valid number.");
                        cardMenu();
                        return;
                    }
                }
                System.out.println("Press Enter to return to the card menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                cardMenu();
                break;
            case "3":
                mainMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                cardMenu();
                break;
        }
    }
}
