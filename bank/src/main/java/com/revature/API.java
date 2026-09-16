package com.revature;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class API {
    // Attributes
    private Scanner s;
    private String accountId;
    private String pin;

    private static String clearScreen = "\n\n\n\n\n";

    private static final Logger logger = LoggerFactory.getLogger(API.class);

    // Constructors

    //////////////
    // Methods ///
    //////////////

    // Connor
    private void launch() {
        System.out.println("Welcome to Bank of CLI!");

        // Query loop to ask the user which command they'd like
        // to execute.
        while (true) {
            System.out.println("To login, type 'l'. To register, type 'r'. To quit, type 'q'.");

            String command = s.nextLine();

            // Login = 'l'
            if (command.equals("l")) {
                // Call login to determine if the login was successful
                //if login was unsuccessful, then reprompt login screen
                if (login()) {
                    break;
                }

            // Register = 'r'
            } else if (command.equals("r")) {
                register();

            // Quit = 'q'
            } else if (command.equals("q")) {
                break;

            // Anything else prompts the user to choose a valid option.
            } else {
                System.out.println("Command unknown. Please choose one of the specified options.");
            }
        }
    }

    // After you register, it should send you back to the launch to login
    // Ydur
    private void register() {
        //Grabs User input as info
        System.out.println("Please Enter your accountId: ");
        accountId = s.nextLine();
        System.out.println("Please Enter your PIN: ");
        pin = s.nextLine();
        //Sends it  Business layer to verify credentials
        try {
            if(Business.verifyRegistration(accountId,pin)){
                System.out.println(accountId +"'s Account created Successfully!");
            }

        } catch (RuntimeException e) {
            System.out.println("This account is taken.");
        } catch (SQLException e){
            System.out.println("The Database communication failed");
        }
    }

    // Yousef
    //for now Business.verifyCredentials() is unimplemented until we work on business layer
    //returns whether or not login was successful
    private boolean login() {
        try {
            System.out.print("Welcome to the login screen. Please provide your Account ID: ");
            String inputAccountId = s.nextLine();
            System.out.print("\nPlease provide your PIN: ");
            String inputPin = s.nextLine();
            Business.verifyCredentials(inputAccountId, inputPin);
            System.out.println(clearScreen);
            System.out.println("Login Successful!");
            //TODO: will need to fix the discrepency with how we use local var accountID and pin with field variabel
            //accountID and pin
            accountId = inputAccountId;
            pin = inputPin;
            homeAccountPage(accountId, pin);
            return true;
        
    // I can catch now a general bank exception without needing to know 
    // exactly which one the business layer will throw
        } catch (BankException e) {
            // instead of printing to console, e will contain very sensitive information/ internal details
            // so we must instead log this information into the logger
            e.printStackTrace();
            // two outcomes: either db failed to process request
            // or Credentials given did not match any records in the db
            // use .getMessage() to print the exception string
            return false;
        }
    }

    // This will be the query loop where it will ask you what you want to do:
    // view balance, deposit, withdraw, transfer, or view activity
    // Damon
    private void homeAccountPage(String accountId, String pin) {
        // Another query loop with those 5 tasks
        // would we want these messages to print each time you get to this page?
        // in that case, if you return from any of the actions, maybe we should move these into the while loop?
        // same sort of reasoning with the text in launch()
        System.out.println("Welcome " + accountId + " to your home page! What would you like to do?");

        /*
            design choice between switch cases and if statements:
            - the switch case would require a boolean variable to determine breaking out of the while loop
         */
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("Please use the following options to perform your actions: \n\n" +
                    "Type 'b' to view your balance.                    || " +
                    "Type 'd' to deposit an amount into your account.\n" +
                    "Type 'w' to withdraw an amount from your account. || " +
                    "Type 't' to transfer an amount from one account to another.\n" +
                    "Type 'v' to view previous account activity.       || " +
                    "Type 'q' to quit to the main menu");
            System.out.print(">>");
            String command = s.nextLine();

            switch (command) {
                case "b":
                    viewBalance();
                    break;
                case "d":
                    // I will leave this implementation like this for now
                    // just unsure whether to ask for the value here or write the logic in the actual method
                    deposit();
                    break;
                case "w":
                    // same reasoning here
                    withdraw();
                    break;
                case "t":
                    // same thing here
                    transfer();
                    break;
                case "v":
                    // same thing here
                    transactionHistory(accountId, pin);
                    break;
                case "q":
                    // same thing here
                    isQuit = true;
                    break;
                default:
                    System.out.println("Not a valid option. Please try again with a valid option.");
                    break;
            }
        }
    }

    // First come first serve for these 5

    // viewBalance: Displays the current balance of the account
    private void viewBalance() {
        System.out.println(clearScreen);
        try {
            System.out.println("Your current balance is: " + Business.viewBalance(this.accountId));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deposit() {
        // Print statement that clears the terminal (depends on which one you're on though; we might need to test this more).
        System.out.print(clearScreen);
        System.out.println(
            "///////////////\n" +
            "/// Deposit ///\n" +
            "///////////////");
        System.out.println("Please input how much you'd like to deposit. Press 'q' to return to the main menu.");
        while (true) {
            System.out.print("$");
            String input = s.nextLine();
            try {
                BigDecimal amount = new BigDecimal(input.trim());
                if (Business.validDeposit(accountId, amount)) {
                    System.out.print("You've deposited $" + amount + ". Thank you!\nRedirecting to home screen...\n");

                    // Wait 3 seconds to clear the terminal and redirect to home screen
                    try {
                        TimeUnit.SECONDS.sleep(4);
                        System.out.print(clearScreen);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                }
                // Would need to move this somewhere later
                System.out.println("Deposit failed! Please try again.");
            } catch (NumberFormatException e) {
                if (input.equals("q")) {
                    System.out.print(clearScreen);
                    break;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (BusinessException e) {
                System.out.println(e.getMessage());
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void withdraw() {
        System.out.println("Please input how much you would like to withdraw");
        while(true){
            System.out.println(clearScreen);
            System.out.println(
                            "///////////////\n" +
                            "/// Withdraw ///\n" +
                            "///////////////");
            System.out.print("$");
            String input = s.nextLine();
            try{
                BigDecimal amount = new BigDecimal(input.trim());
                try{
                    //Business Layer - Call a  func to validate withdraw amount
                    Business.validWithdraw(accountId, amount);
                    System.out.println(clearScreen);
                    System.out.println("$"+ amount + " has been successfully withdrawn from your account.");
                }
                catch (BankException e){
                    System.out.println(clearScreen);
                    System.out.println("Withdrawal failed due to: " + e);
                }
            }
            catch (NumberFormatException e){
                if(input.equals("q")) {
                    System.out.println(clearScreen);
                    break;
                }
                System.out.println(clearScreen);
                System.out.println("Please enter a valid numeric value (Decimals are valid)");
            }
        }



    }
    // Ye
    private void transfer() {
        System.out.println("Please input the account ID you'd like to transfer to.");
        String toId = s.nextLine();
        System.out.println("Please input transfer amount.");
        BigDecimal amount = new BigDecimal(s.nextLine().trim());
        // Business Layer validates transaction
        // Business.transfer(fromId, toId, amount)
        System.out.println("You've transferred $" + amount + " to " + toId + ".");
    }

    //yousef
    // displays transaction activity from db
    private void transactionHistory(String accountId, String pin) {
        final int pageSize = 5;
        try {
            ArrayList<Transaction> res = Business.validateTransactionHistory(accountId, pin);
            if (res.isEmpty()) {
                System.out.println("History is empty");
                return;
            }
            int page = 0;
            boolean browsing = true;
            while (browsing) {
                System.out.print(clearScreen);
                int start = page * pageSize;
                int end = Math.min(start + pageSize, res.size());
                System.out.println("Transaction History");
                System.out.println("-------------------");
                for (int i = start; i < end; i++) {
                    Transaction transaction = res.get(i);
                    switch (transaction.getType()) {
                        case "DEPOSIT":
                            System.out.println("Deposited $" + transaction.getAmount()
                                    + " on " + transaction.getCreationDate());
                            break;
                        case "WITHDRAWAL":
                            System.out.println("Withdrew $" + transaction.getAmount()
                                    + " on " + transaction.getCreationDate());
                            break;
                        case "TRANSFER_IN":
                            System.out.println("Received $" + transaction.getAmount()
                                    + " from " + transaction.getRelatedAccountId()
                                    + " on " + transaction.getCreationDate());
                            break;
                        case "TRANSFER_OUT":
                            System.out.println("Transferred $" + transaction.getAmount()
                                    + " to " + transaction.getRelatedAccountId()
                                    + " on " + transaction.getCreationDate());
                            break;
                        default:
                            System.out.println("Unknown transaction type: " + transaction.getType());
                            break;
                    }
                }
                int totalPages = (res.size() + pageSize - 1) / pageSize;
                System.out.println("\nPage " + (page + 1) + " of " + totalPages);
                System.out.println("[n] Next  [p] Previous  [q] Quit");
                System.out.print(">>");
                String command = s.nextLine().toLowerCase();
                switch (command) {
                    case "n":
                        if (end < res.size()) {
                            page++;
                        } else {
                            System.out.println();
                            System.out.println();
                            System.out.println("You are on the last page!");
                            waitALittle(2);
                        }
                        break;
                    case "p":
                        if (page > 0) {
                            page--;
                        } else {
                            System.out.println();
                            System.out.println();
                            System.out.println("You are on the first page.!");
                            waitALittle(2);
                        }
                        break;
                    case "q":
                        browsing = false;
                        break;
                    default:
                        System.out.println();
                        System.out.println();
                        System.out.println("Invalid option!");
                        waitALittle(2);
                        break;
                }
            }
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }

    private void waitALittle(int seconds){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Main
    public void run() {
        // Might want to use the Singleton pattern in the future,
        // but considering there aren't multiple threads involved,
        // it may not be needed.
        s = new Scanner(System.in);

        // Run the launch function
        this.launch();

        // Close the scanner once the application closes;
        // Might need to change this in the future.
        s.close();
    }
}
