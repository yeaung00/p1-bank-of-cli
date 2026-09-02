package com.revature;

import java.util.*;

public class API {
    // Attributes
    private Scanner s;

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
    }

    // Yousef
    // Class is private because method should only be accessed within class (API) and not outside
    private void login(String AcountID, String PIN) {
        try {
            if(Business.verifyCredentials(AcountID, PIN)) {
                System.out.println("Login Successful!");
                // Would call homeAccountPage here
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
    public void homeAccountPage() {
        // Another query loop with those 5 tasks
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
