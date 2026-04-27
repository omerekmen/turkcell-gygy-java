package com.turkcell;

import java.util.Scanner;

import com.turkcell.model.User;
import com.turkcell.repository.BankAccountRepository;
import com.turkcell.repository.BankCardRepository;
import com.turkcell.repository.CreditCardRepository;
import com.turkcell.repository.IMBankAccountRepository;
import com.turkcell.repository.IMBankCardRepository;
import com.turkcell.repository.IMCreditCardRepository;
import com.turkcell.repository.IMUserRepository;
import com.turkcell.repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new IMUserRepository();
        BankAccountRepository bankAccountRepository = new IMBankAccountRepository();
        CreditCardRepository creditCardRepository = new IMCreditCardRepository();
        BankCardRepository bankCardRepository = new IMBankCardRepository();

        loadMockData(userRepository, bankAccountRepository);

        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("Banking application started successfully.");
        System.out.println("Please log in to your account.");
        System.out.println("------------------------------------------------------------------------ \n");

        Scanner scanner = new Scanner(System.in);
        
        User loggedInUser = login(userRepository, bankAccountRepository, scanner);
        Operations operations = new Operations(scanner, loggedInUser, userRepository, bankAccountRepository, creditCardRepository, bankCardRepository);
        operations.mainMenu();

        scanner.close();
        System.out.println("------------------------------------------------------------------------ \n");
    }

    // This method handles both login and new user registration based on TCKN input
    public static User login( 
        UserRepository userRepository, 
        BankAccountRepository bankAccountRepository, 
        Scanner scanner
    ) {
        int attempts = 0;
        String tckn = null;
        while (true) {
            if (tckn == null) {
                System.out.println("Enter TCKN:");
                tckn = scanner.nextLine();

                if (!userRepository.checkTcknRules(tckn)) {
                    System.out.println("Invalid TCKN format. TCKN must be exactly 11 digits. Please try again.");
                    tckn = null;
                    continue;
                }
            }

            boolean isExistingUser = userRepository.checkUserExists(tckn);
            if (!isExistingUser) {
                System.out.println("User with TCKN " + tckn + " does not exist. We will create a new account for you.");
                System.out.println("Enter your name:");
                String name = scanner.nextLine();
                System.out.println("Enter a password (at least 6 characters):");
                String password = scanner.nextLine();
                userRepository.addUser(name, tckn, password);
                bankAccountRepository.createAccount(tckn, 0.0);
                System.out.println("\nAccount created successfully. Please log in again.");
                continue; // Skip the rest of the loop and prompt for login again
            }

            System.out.println("Enter password:");
            String password = scanner.nextLine();

            User user = userRepository.getUser(tckn, password);
            if (user != null) {
                return user; // Return the logged-in user
            } else {
                System.out.println("Invalid TCKN or password. Please try again. (Attempt " + (attempts + 1) + "/3)");
                attempts++;
                if (attempts >= 3) {
                    System.out.println("Too many failed login attempts. Exiting application.");
                    System.exit(0); // Exit the application after 3 failed attempts
                }
            }
        }
    }

    public static void loadMockData(
        UserRepository userRepository, 
        BankAccountRepository bankAccountRepository
    ) {
        try {
            String user1_tckn = userRepository.addUser("John Doe", "12345678901", "password123");
            String user2_tckn = userRepository.addUser("Jane Smith", "19876543210", "password456");
            String user3_tckn = userRepository.addUser("Alice Johnson", "11223344556", "password789");
            String u1_account_number = bankAccountRepository.createAccount(user1_tckn, 1000.0);
            String u2_account_number = bankAccountRepository.createAccount(user2_tckn, 2000.0);
            String u3_account_number = bankAccountRepository.createAccount(user3_tckn, 3000.0);

            System.out.println("\n------------------------------------------------------------------------");
            System.out.println("Mock data loaded successfully.");
            System.out.println("User 1 TCKN: " + user1_tckn + ", Account Number: " + u1_account_number + ", Balance: " + bankAccountRepository.getAccount(u1_account_number).getBalance());
            System.out.println("User 2 TCKN: " + user2_tckn + ", Account Number: " + u2_account_number + ", Balance: " + bankAccountRepository.getAccount(u2_account_number).getBalance());
            System.out.println("User 3 TCKN: " + user3_tckn + ", Account Number: " + u3_account_number + ", Balance: " + bankAccountRepository.getAccount(u3_account_number).getBalance());
            System.out.println("------------------------------------------------------------------------ \n");

        } catch (Exception e) {
            System.out.println("Could not load user: " + e.getMessage());
        }
    }
}
