package com.revature;

public class Business {
    // Ydur
    public static boolean verifyRegistration(String accountID) {
        return true;
    }

    // Yousef
    public static boolean verifyCredentials(String AcountID, String PIN) {
        return true;
    }

    // Gets the balance from the Repository layer
    public static double viewBalance(String accountID) {
        // assuming that there will be a database connection to retrieve the balance for the given accountID
        // for now, we will just print a mock balance
        double balance = 1000.00; // Mock balance for now
        /*
            Generally, how it will look with a database connection:
            double balance = database.getBalance(accountID);
            System.out.println("Your current balance is: $" + balance);
         */
        return balance;
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
