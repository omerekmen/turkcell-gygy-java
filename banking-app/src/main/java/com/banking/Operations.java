package com.banking;

import java.util.Scanner;

import com.banking.model.User;
import com.banking.model.BankAccount;
import com.banking.repository.UserRepository;
import com.banking.repository.BankAccountRepository;

public class Operations {
    Scanner scanner;
    User loggedInUser;
    UserRepository userRepository;
    BankAccountRepository bankAccountRepository;

    public Operations(Scanner scanner, User loggedInUser, UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.scanner = scanner;
        this.loggedInUser = loggedInUser;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void mainMenu() {
        while (loggedInUser != null) {
            System.out.println("\n------------------------------------------------------------------------");
            System.out.println("\nWelcome to your banking dashboard, " + loggedInUser.getName() + "!");
            
            System.out.println("Please select an option:");
            System.out.println("1. View account details");
            System.out.println("2. Make a transaction");
            System.out.println("3. View Credit Card Details");
            System.out.println("4. Log out");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\n------------------------------------------------------------------------");
                    System.out.println("Viewing account details...");
                    System.out.println("------------------------------------------------------------------------ \n");
                    
                    getAccountDetails(loggedInUser, bankAccountRepository);

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
                    System.out.println("Viewing credit card details...");
                    System.out.println("------------------------------------------------------------------------ \n");

                    System.out.println("------------------------------------------------------------------------ \n");
                    System.out.println("Press Enter to return to the main menu...");
                    scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                    continue;
                case "4":
                    System.out.println("Logging out...");
                    loggedInUser = null;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue;
            }
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

                System.out.println("------------------------------------------------------------------------ \n");
                System.out.println("Press Enter to return to the transaction menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                transactionMenu();
                break;
            case "2":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Withdrawing money...");
                System.out.println("------------------------------------------------------------------------ \n");

                System.out.println("------------------------------------------------------------------------ \n");
                System.out.println("Press Enter to return to the transaction menu...");
                scanner.nextLine(); // Wait for user to press Enter before showing the menu again
                transactionMenu();
                break;
            case "3":
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("Transferring money...");
                System.out.println("------------------------------------------------------------------------ \n");

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

    public static void getAccountDetails(User loggedInUser, BankAccountRepository bankAccountRepository) {
        BankAccount account = bankAccountRepository.getAccountByTckn(loggedInUser.getTckn());
        if (account != null) {
            System.out.println("Name: " + loggedInUser.getName());
            System.out.println("TCKN: " + loggedInUser.getTckn());
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Balance: " + account.getBalance());
        } else {
            System.out.println("No bank account found for this user.");
        }
    }
}
