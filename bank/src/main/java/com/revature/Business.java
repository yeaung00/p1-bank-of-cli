package com.revature;

public class Business {
    public static boolean verifyCredentials(String AcountID, String PIN) {
        return true;
    }

    // Gets the balance from the Repository layer
    public static void viewBalance(String accountID) {

    }
    
    // Checks if the deposit is valid (Is the amount positive?)
    public static boolean validDeposit(String accountID, double amount) {
        return true;
    }

    // Checks if the withdraw is valid (Do they have enough? Is the amount positive?)
    public static boolean validWithdraw(String accountID, double amount) {
        //If amount is more than in the account, a negative number,a non number , invalid
        if(amount > 5 ) {
            return true;
        }else {
            return false;
        }
    }

    // Checks if the transfer is valid (Does the other person have enough? Do you? Is the amount positive?)
    public static boolean validTransfer(String accountIDFrom, String accountIDTo, double amount) {
        return true;
    }

    // Gets the account activity from the repository layer
    public static void viewActivity(String accountID) {

    }
}
