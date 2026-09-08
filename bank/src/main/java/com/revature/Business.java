package com.revature;

import java.util.HashMap;

public class Business {
    private HashMap mockDB;

    public Business() {
        mockDB = new HashMap<String, String>();
        // putting random accountIDs and PINs into the mock database for testing purposes
        mockDB.put("123456", "1234");
        mockDB.put("654321", "4321");
        mockDB.put("111111", "1111");
    }

    public static boolean verifyCredentials(String accountID, String PIN) {
        return true;
    }

    public static void viewBalance(String accountID) {
        // assuming that there will be a database connection to retrieve the balance for the given accountID
        // for now, we will just print a mock balance
        System.out.println("Your current balance is: $1000.00");

        /*
            Generally, how it will look with a database connection:
            double balance = database.getBalance(accountID);
            System.out.println("Your current balance is: $" + balance);
         */
    }
}
