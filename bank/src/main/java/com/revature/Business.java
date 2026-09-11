package com.revature;

public class Business {
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
        if(accountID.equals("Billy")){
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
        return 0.0;
    }
    
    // Checks if the deposit is valid (Is the amount positive?) - Connor
    public static boolean validDeposit(String accountID, double amount) {
        return true;
    }

    // Checks if the withdraw is valid (Do they have enough? Is the amount positive?) - Ydur
    public static boolean validWithdraw(String accountID, double amount) {
        return true;
    }

    // Checks if the transfer is valid (Does the other person have enough? Do you? Is the amount positive?) - Ye
    public static boolean validTransfer(String accountIDFrom, String accountIDTo, double amount) {
        return true;
    }

    // Gets the account activity from the repository layer - Yousef
    public static void viewActivity(String accountID) {

    }
}
