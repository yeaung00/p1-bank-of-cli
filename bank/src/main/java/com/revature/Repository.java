package com.revature;

import com.revature.utility.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

public class Repository {
    // Adds a new account after a user registers - Ydur
    public static void addAccount() {

    }

    // Gets the accountID and PIN to verify the credentials when logging in - Yousef
    public static Account getAccount(String AccountID, String pin) throws AccountNotFoundException, DatabaseException {
        String query = "select account_id, pin_hash " +
                        "from accounts " + 
                        "where account_id = ? " +
                        "and pin_hash = ?";
        String debugAccID = "select account_id " +
                            "from accounts " + 
                            "where account_id = ? " +
                            "";
        String debugPin = "select pin_hash " +
                          "from accounts " +
                          "where pin_hash = ?";
        String resAccountID = null;
        String resPin = null;
        try(Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement statement = connection.prepareStatement(query); ){
                statement.setString(1, AccountID);
                statement.setString(2, pin);
                ResultSet res = statement.executeQuery();
                if(res.next()) {
                    resAccountID = res.getString("account_id");
                    resPin = res.getString("pin_hash");
                    return new Account(resAccountID,resPin);
                }
                else {
                    PreparedStatement checkAccID = connection.prepareStatement(debugAccID);
                    checkAccID.setString(1, AccountID);
                    PreparedStatement checkPin = connection.prepareStatement(debugPin);
                    checkPin.setString(1, pin);
                    ResultSet resAccID = checkAccID.executeQuery();
                    ResultSet resP = checkPin.executeQuery();
                    boolean hasCorrectAcc = resAccID.next();
                    boolean hasCorrectPin = resP.next();
                    checkAccID.close();
                    checkPin.close();
                    // this is where we would log to the debugger which credential was not correct.
                    //it is important to not pass this logging down to the console as
                    //this is information only we the developers should be able to see
                    if(!hasCorrectAcc && hasCorrectPin) {
                        throw new AccountNotFoundException("No record contains the Account ID " + AccountID + " in the accounts table.");
                    }
                    else if (!hasCorrectPin && hasCorrectAcc) {
                        throw new AccountNotFoundException("No record contains the pin " + pin + " in the accounts table");
                    }
                    else {
                        throw new AccountNotFoundException("No record contains the Account ID " + AccountID +
                                                            " nor does any record contain the pin " + pin + 
                                                            " in the accounts table");
                    }

                }
            
        } catch(SQLException e) {
           throw new DatabaseException("Database error during Account retrieval: ", e);
        }
    }

    // Might not even need this - Yousef
    private static void getBalance() {

    }

    // (updateBalance) Updates the value of balance during deposits and withdraws - Connor
    public static void updateBalance(String accountID, double amount) {

    }

    // Perform manualCommitConnect() and facilitate a transfer - Ye
    public static void transfer(String accountFrom, String accountTo, double amount) {

    }

    // Adds a transaction for an associated accountID - Ye
    public static void addTransaction() {

    }

    // Retrieves the tranactions for an associated accountID - Yousef
    public static void viewTransactionHistory() {

    }
}
