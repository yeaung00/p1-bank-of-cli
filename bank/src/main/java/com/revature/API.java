package com.revature;

import java.util.*;

import com.revature.Exceptions.NegativeInputException;
import com.revature.Exceptions.MoreThanTwoDecimalPlacesException;
import com.revature.Exceptions.InvalidCredentialsException;
import java.util.concurrent.TimeUnit;

public class API {
    // Attributes
    private Scanner s;
    private String accountID;
    private String pin;
    private HashMap mockDB;

    private static String clearScreen = "\033[2J";

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
        System.out.println("Please Enter your accountID: ");
        accountID = s.nextLine();
        System.out.println("Please Enter your PIN: ");
        pin = s.nextLine();
        if(accountID.equals("Billy")){
            System.out.println("This will check to see accountID is taken.");
        }else if (accountID == null || accountID.isEmpty()){
            System.out.println("This will check if it's empty.");
//            System.out.println("Please enter an accountID.");
        }else {
//            mockDB.put(accountID,pin);
            System.out.println("We would put it into the database");
        }
    }

    // Yousef
    //for now Business.verifyCredentials() is unimplemented until we work on business layer
    //returns whether or not login was successful
    private boolean login() {
        try {
            System.out.print("Welcome to the login screen. Please provide your Account ID: ");
            String accountID = s.nextLine();
            System.out.print("\nPlease provide your PIN: ");
            String pin = s.nextLine();
            Business.verifyCredentials(accountID, pin);
            System.out.println("Login Successful!");
            homeAccountPage(accountID);
            return true;

        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    // This will be the query loop where it will ask you what you want to do:
    // view balance, deposit, withdraw, transfer, or view activity
    // Damon
    private void homeAccountPage(String accountID) {
        // Another query loop with those 5 tasks
        // would we want these messages to print each time you get to this page?
        // in that case, if you return from any of the actions, maybe we should move these into the while loop?
        // same sort of reasoning with the text in launch()
        System.out.println("Welcome " + accountID + " to your home page! What would you like to do?");

        /*
            design choice between switch cases and if statements:
            - the switch case would require a boolean variable to determine breaking out of the while loop
         */
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("Please use the following options to perform your actions: \n" +
                    "Type 'b' to view your balance.\n" +
                    "Type 'd' to deposit an amount into your account.\n" +
                    "Type 'w' to withdraw an amount from your account.\n" +
                    "Type 't' to transfer an amount from one account to another.\n" +
                    "Type 'v' to view previous account activity.\n" +
                    "Type 'q' to quit to the main menu");
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
                    transactionHistory(accountID);
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
        // This method will call the business layer to get the balance of the account
//        System.out.println("Your current balance is: [insert value here]");

        // dummy business call
        Business.viewBalance(this.accountID);
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
                double amount = Double.parseDouble(input);
                if (Business.validDeposit(accountID, amount)) {
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
            } catch (NegativeInputException e) {
                System.out.println(e.getMessage() + "Please try again.");
            } catch (MoreThanTwoDecimalPlacesException e) {
                System.out.println(e.getMessage() + "Please try again.");
            }
        }
    }

    private void withdraw() {
        System.out.println("Please input how much you would like to withdraw");
        double amount = Double.parseDouble(s.nextLine());
        //Business Layer - Calla  func to validate withdraw amount
        System.out.println("$"+ amount + " has been successfully withdrawn from your account.");
    }
    // Ye
    private void transfer() {
        System.out.println("Please input the account ID you'd like to transfer to.");
        String  toId = s.nextLine();
        System.out.println("Please input transfer amount.");
        double amount = Double.parseDouble(s.nextLine());
        // Business Layer validates transaction
        // Business.transfer(fromId, toId, amount)
        System.out.println("You've transferred $" + amount + " to " + toId + ".");
    }

    //yousef
    // displays transaction activity from db
    private void transactionHistory(String accountID) {
        //temporarily adding info into transaction history
        try{
            String res = Business.validateTransactionHistory(accountID);
            System.out.println(res);
        } catch(Exception e) {
            System.out.println("Error:" + e);
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
