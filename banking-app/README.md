# Banking App

A simple Java 21 console banking application built with Maven. It is designed as a small training project with in-memory storage and a text-based menu flow.

## Overview

The app starts with a login screen that also handles new user registration when the entered TCKN does not exist yet. After login, the user can navigate a console dashboard to manage a single bank account.

## Features

- Login and registration by TCKN
- Basic TCKN and password validation
- Mock data loaded at startup
- View account details
- Deposit money
- Withdraw money
- Transfer money to another account
- In-memory repositories for users and bank accounts

## Project Structure

```text
banking-app/
├─ pom.xml
├─ Makefile
└─ src/
   └─ main/
      ├─ java/
      │  └─ com/banking/
      │     ├─ Main.java
      │     ├─ Operations.java
      │     ├─ model/
      │     └─ repository/
      └─ resources/
         └─ mock_data.json
```

## Requirements

- Java 21
- Maven

## Run

From this folder:

```bash
mvn clean compile
java -cp target/classes com.banking.Main
```

Or use the provided Makefile:

```bash
make compile
make runclass
make full
```

## Application Flow

1. Start the application.
2. Enter a valid 11-digit TCKN.
3. Log in with an existing password, or create a new account when prompted.
4. Use the dashboard to view details or make transactions.
5. Log out to exit the program.

## Notes

- User and account data are stored in memory only.
- Restarting the application resets all runtime changes.
- Mock users and balances are loaded when the program starts.
- Account numbers are generated automatically starting from `1000000000`.
