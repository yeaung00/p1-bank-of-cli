package com.revature;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class Business {

    private static final Logger logger = LoggerFactory.getLogger(Business.class);

    // Ydur
    //accountId should be 10 chars, maybe pin can be verified aswell later

    public static boolean verifyRegistration(String accountId,String pin) throws InvalidCredentialsException {
        //Checks if the length of the name is long enough for an account to be created
        if(accountId.length() > 10 ){
            throw new InvalidCredentialsException("Account Id is too long!");
        }
        //If check passes, Passes to Repository layer to checkExistingAccounts() & will addAccount if it passes
        try {
            //If checkExistingAccounts returns false meaning no records with that account exist, run addAccount
            if(!Repository.checkExistingAccounts(accountId)){
                Repository.addAccount(accountId,pin);
            }else{
                return false;
            }
        } catch (DatabaseException e) {
            throw new InvalidCredentialsException("There was an issue communicating wih the Database: ",e);
        }
        return true;
    }

    // Yousef
    // This method verifies both the account id and pin pair by checking the db if it contains it
    // This method can throw an exception if the repo layer encounters an error with not finding the respective credentials
    public static boolean verifyCredentials(String accountId, String pin) throws InvalidCredentialsException, DatabaseException {
        // make call to repository layer to check for AccountID and pin pair in future
        try{
            Account res = Repository.getAccount(accountId, pin);

            return(res.getAccountId().equals(accountId) && res.getPin().equals(pin));

        } catch (RepositoryException e) {
            if (e instanceof AccountNotFoundException) {
                throw new InvalidCredentialsException("Invalid Account ID or PIN", e);
            }
            if (e instanceof DatabaseException databaseException) {
                throw databaseException;
            }
            return false;
        }
    }

    // Gets the balance from the Repository layer - Damon
    public static BigDecimal viewBalance(String accountID) throws AccountNotFoundException, DatabaseException, InvalidCredentialsException {
        // interesting interaction that I'm not sure if it needs to be fixed
        // viewBalance doesn't ever have to interact with an Account object:
        // only ever querying the database and returning that value
        try {
            return Repository.getBalance(accountID);
        } catch (RepositoryException e) {
            if (e instanceof AccountNotFoundException) {
                throw new InvalidCredentialsException("Invalid accountID", e);
            }
            if (e instanceof DatabaseException dbException) {
                throw dbException;
            }

            // this return value can be changed for better logging/error purposes
            return BigDecimal.ZERO;
        }
    }
    
    // Checks if the deposit is valid (Is the amount positive?) - Connor
    public static boolean validDeposit(String accountId, BigDecimal amount) throws BusinessException, RepositoryException {
        // If the amount is negative, it is not a valid deposit
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInputException("You cannot input a negative amount to deposit. Please try again");
        } else if (!hasAtMostTwoDecimalPlaces(amount)) {
            throw new MoreThanTwoDecimalPlacesException("The amount cannot have more than two decimal places. Please try again.");
        }

        // Send a request to the repo layer to update the balance to total
        BigDecimal total = viewBalance(accountId).add(amount);
        Repository.updateBalance(accountId, total);
        System.out.println("This is where the Account object would update the balance");

        // Return true if everything above succeeds
        return true;
    }

    /* Deposit (and perhaps withdraw) helper function
     * ----------------------------------------------
     *
     * Checks if the amount you want to deposit has at most two decimal places.
     * E.g, $100.45 would return true, $100.456 would return false.
     */
    private static boolean hasAtMostTwoDecimalPlaces(BigDecimal amount) {
        return amount.scale() <= 2;
    }

    // Checks if the withdrawal is valid (Do they have enough? Is the amount positive?) - Ydur
    public static boolean validWithdraw(String accountId, BigDecimal amount) throws InsufficientFundsException, NegativeInputException, MoreThanTwoDecimalPlacesException, InvalidCredentialsException, AccountNotFoundException, DatabaseException, RepositoryException{
        //If amount is more than in the account, a negative number,a non number , has too many decimal places,throw error


        if(amount.compareTo(viewBalance(accountId)) > 0) {
            throw new InsufficientFundsException("The amount withdrawn cannot be more than the account balance.");
        } else if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInputException("Unable to withdraw a negative amount.");
        }
        else if(!hasAtMostTwoDecimalPlaces(amount)){
            throw new MoreThanTwoDecimalPlacesException("There were too many decimal places provided.");
        }

        try {
            BigDecimal total = viewBalance(accountId).subtract(amount);
            Repository.updateBalance(accountId,total);
        } catch (InvalidCredentialsException | RepositoryException e) {
            throw new RuntimeException(e);
        }

        return true;

    }

    // Checks if the transfer is valid (Does the other person have enough? Do you? Is the amount positive?) - Ye
    public static boolean validTransfer(String accountIdFrom, String accountIdTo, BigDecimal amount) {
        return true;
    }

    // Gets the account activity from the repository layer - Yousef
    // for now just returns a string, but in future will return rows of data
    //maybe can create class representing a row of data then use a collections class like
    //Arraylist to store the data
    //ArrayList<Transaction> = new ArrayList<>();
    public static ArrayList<Transaction> validateTransactionHistory(String accountID,String pin) throws InvalidCredentialsException, EmptyTransactionHistoryException, DatabaseException{
        verifyCredentials(accountID, pin);
        return Repository.getTransactionHistory(accountID);
    }
}
