package com.revature;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

import java.math.BigDecimal;

public class Business {
    public static Account a;

    // Ydur
    public static boolean verifyRegistration(String accountID) {
        return true;
    }

    // Yousef
    // This method verifies both the account id and pin pair by checking the db if it contains it
    // This method can throw an exception if the repo layer encounters an error with not finding the respective credentials
    public static boolean verifyCredentials (String accountID, String pin) throws InvalidCredentialsException {
        // make call to repository layer to check for AccountID and pin pair in future
        // for now just check if accountID = "Billy" to test all branch flows

        // When testing, login with accoundID "Billy" and Pin "4"
        if(accountID.equals("Billy") && pin.equals("4")){
            a = new Account(accountID, "4", viewBalance(accountID));
            return true;
        }
        else {
            //We should make the message for the exceptions in the business layer more verbose for better debugging,
            //then when we pass the exception down to the api layer, make it less verbose to hide implementation detail
            //ex: we could have the message here explain exactly which part was invalid ("account id, pin, or both")
            throw new InvalidCredentialsException("Invalid Account ID or PIN");
        }
    }

    // Gets the balance from the Repository layer - Damon
    public static double viewBalance(String accountID) {
        // assuming that there will be a database connection to retrieve the balance for the given accountID
        // for now, we will just print a mock balance
        /*
            Generally, how it will look with a database connection:
            double balance = database.getBalance(accountID);
            System.out.println("Your current balance is: $" + balance);
         */
        return a.getBalance();
    }
    
    // Checks if the deposit is valid (Is the amount positive?) - Connor
    public static boolean validDeposit(String accountID, double amount) throws NegativeInputException, MoreThanTwoDecimalPlacesException {
        // If the amount is negataive, it is not a valid deposit
        if (amount < 0) {
            throw new NegativeInputException("You cannot input a negative amount to deposit. ");
        } else if (!hasAtMostTwoDecimalPlaces(amount)) {
            throw new MoreThanTwoDecimalPlacesException("The amount cannot have more than two decimal places. ");
        }

        // Send a request to the repo layer to update the balance to total
        double total = viewBalance(accountID) + amount;
        System.out.println("This would send the deposit request to the Repo layer...");
        a.setBalance(total);

        // Update the account's balance and return true if everything succeeded.
        a.setBalance(total);
        return true;
    }

    /* Deposit (and perhaps withdraw) helper function
     * ----------------------------------------------
     *
     * Checks if the amount you want to deposit has at most two decimal places.
     * E.g, $100.45 would return true, $100.456 would return false.
     */
    private static boolean hasAtMostTwoDecimalPlaces(double amount) {
        // Convert the double to a String, and then to a BigDecimal
        String text = Double.toString(amount);
        BigDecimal bd = new BigDecimal(text);

        // Checks if the amount has at most 2 decimal places (can't deposit $100.345)
        int decimalPlaces = bd.scale();
        return decimalPlaces >= 0 && decimalPlaces <= 2;
    }

    // Checks if the withdrawal is valid (Do they have enough? Is the amount positive?) - Ydur
    public static boolean validWithdraw(String accountID, double amount) throws InsufficientFundsException, NegativeInputException, MoreThanTwoDecimalPlacesException {
        //If amount is more than in the account, a negative number,a non number , has too many decimal places,throw error
        if(amount > Business.viewBalance(accountID)) {
            throw new InsufficientFundsException("The amount withdrawn cannot be more than the account balance.");
        }else if(amount < 0){
            throw new NegativeInputException("Unable to withdraw a negative amount.");
        }
        else if(!hasAtMostTwoDecimalPlaces(amount)){
            throw new MoreThanTwoDecimalPlacesException("There were too many decimal places provided.");
        }

        double total = viewBalance(accountID) - amount;
        System.out.println("This would send the withdrawl request to the Repo layer...");

        // Update the account's balance and return true if everything succeeded.
        a.setBalance(total);
        return true;
    }

    // Checks if the transfer is valid (Does the other person have enough? Do you? Is the amount positive?) - Ye
    public static boolean validTransfer(String accountIDFrom, String accountIDTo, double amount) throws InsufficientFundsException, NegativeInputException, MoreThanTwoDecimalPlacesException {
        if (amount > Business.viewBalance(accountIDFrom)) {
            throw new InsufficientFundsException("The amount withdrawn cannot be more than the account balance.");
        } else if (amount < 0){
            throw new NegativeInputException("Unable to withdraw a negative amount.");
        } else if (!hasAtMostTwoDecimalPlaces(amount)){
            throw new MoreThanTwoDecimalPlacesException("There were too many decimal places provided.");
        }

        double totalFrom = viewBalance(accountIDFrom) - amount;
        double totalTo = viewBalance(accountIDTo) + amount;
        System.out.println("This would send the ATOMIC TRANSFER to the repo layer...");

        // Update the current account's balance and return true if everything succeeded.
        a.setBalance(totalFrom);
        return true;
    }

    // Gets the account activity from the repository layer - Yousef
    // for now just returns a string, but in future will return rows of data
    //maybe can create class reprenting a row of data then use a collections class like
    //Arraylist to store the data
    //ArrayList<Transaction> = new ArrayList<>();
    public static String validateTransactionHistory(String accountID) throws EmptyTransactionActivityException{
        // just testing full logic flow, we would call a method(like getTransactionActivity()) in repository layer here that fetches
        // transaction activity
        if(accountID.equals("Billy")) {
            return "here we would return data from db";
        }
        else {
            throw new EmptyTransactionActivityException("No transaction activity found for account: " + accountID);
        }
    }
}
