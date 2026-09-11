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
