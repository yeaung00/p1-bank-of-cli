package com.revature;

import java.util.*;

public class API {
    // Attributes
    private Scanner s;
    private String accountID;
    private String pin;
    private HashMap mockDB;

    // Constructors

    //////////////
    // Methods ///
    //////////////

    // Connor
    public void launch() {
        System.out.println("Welcome to Bank of CLI!");
        System.out.println("To login, type 'l'. To register, type 'r'. To quit, type 'q'.");

        // Query loop to ask the user which command they'd like
        // to execute.
        while (true) {
            String command = s.nextLine();

            // Login = 'l'
            if (command.equals("l")) {
                // Prompt the user for their account ID and PIN
                System.out.print("Welcome to the login screen. Please provide your Account ID: ");
                String accountID = s.nextLine();
                System.out.print("\nPlease provide your PIN: ");
                String pin = s.nextLine();

                // Call login to determine if the login was successful
                login(accountID, pin);

                // Leads to the application quitting
                break;

            // Register = 'r'
            } else if (command.equals("r")) {
                register();
                break;

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
    public void register() {
        System.out.println("Please Enter your accountID: ");
        accountID = s.nextLine();
        System.out.println("Please Enter your PIN: ");
        pin = s.nextLine();
        if(mockDB.containsKey(accountID)){
            System.out.println("This accountID is taken.");
        }else if (accountID == null || accountID.isEmpty()){
            System.out.println("Please enter an accountID.");
        }else {
            mockDB.put(accountID,pin);
        }
    }

    // Yousef
    // Class is private because method should only be accessed within class (API) and not outside
    private void login(String accountID, String pin) {
        try {
            if(Business.verifyCredentials(accountID, pin)) {
                System.out.println("Login Successful!");
                homeAccountPage(accountID);
            }
            else {
                System.out.println("Username or password is incorrect, please try again");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
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
        System.out.println("Please use the following options to perform your actions: \n" +
                "Type 'b' to view your balance.\n" +
                "Type 'd' to deposit an amount into your account.\n" +
                "Type 'w' to withdraw an amount from your account.\n" +
                "Type 't' to transfer an amount from one account to another.\n" +
                "Type 'v' to view previous account activity.\n" +
                "Type 'q' to quit to the main menu");

        /*
            design choice between switch cases and if statements:
            - the switch case would require a boolean variable to determine breaking out of the while loop
         */
        boolean isQuit = false;
        while (!isQuit) {
            String command = s.nextLine();

            switch (command) {
                case "b":
                    viewBalance(accountID);
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
                    viewActivity();
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
    public void viewBalance() {
    }

    public void deposit() {
    }

    public void withdraw() {
    }

    public void transfer() {
    }

    public void viewActivity() {
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
