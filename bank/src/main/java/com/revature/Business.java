package com.revature;

import com.revature.Exceptions.NegativeInputException;
import com.revature.Exceptions.MoreThanTwoDecimalPlacesException;
import java.math.BigDecimal;

public class Business {
    // Ydur
    public static boolean verifyRegistration(String accountID) {
        return true;
    }

    // Yousef
    public static boolean verifyCredentials(String AcountID, String PIN) {
        return true;
    }

    // Gets the balance from the Repository layer - Damon
    public static double viewBalance(String accountID) {
        return 0.0;
    }
    
    // Checks if the deposit is valid (Is the amount positive?) - Connor
    public static boolean validDeposit(String accountID, double amount) throws NegativeInputException, MoreThanTwoDecimalPlacesException {
        // If the amount is negataive, it is not a valid deposit
        if (amount < 0) {
            throw new NegativeInputException("You cannot input a negative amount to deposit. ");
        } else if (hasAtMostTwoDecimalPlaces(amount)) {
            throw new MoreThanTwoDecimalPlacesException("The amount cannot have more than two decimal places. ");
        }

        // Send a request to the repo layer to update the balance to total
        double total = viewBalance(accountID) + amount;
        System.out.println("This would send the deposit request to the Repo layer...");

        // Return true if everything above succeeds
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
        return decimalPlaces < 0 || decimalPlaces > 2;
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
