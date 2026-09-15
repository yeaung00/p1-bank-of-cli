package com.revature;

import com.revature.exceptions.RepositoryException;
import com.revature.utility.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {
    // Adds a new account after a user registers - Ydur
    public static void addAccount() {

    }

    // Gets the accountID and PIN to verify the credentials when logging in - Yousef
    public static void getAccount() {

    }

    // Might not even need this - Yousef
    public static double getBalance(String accountID) throws AccountNotFoundException, DatabaseException {
        // assuming that accountID is unique
        String query = "SELECT balance_cents" +
                        "FROM accounts " +
                        "WHERE account_id = ?";

        try (
                Connection conn = ConnectionFactory.getAutoCommitConnect();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, accountID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // process the result
                return rs.getInt("balance_cents") / 100.0;
            } else {
                // TODO: similarly to what Yousef specified: this is where the logging would be

                // assuming the AccountNotFoundException is implemented
                throw new AccountNotFoundException("Account not found: " + accountID);
            }
        } catch (SQLException e) {
            // assuming the DatabaseException is implemented
            throw new DatabaseException("Error occurred while fetching account balance", e);
        }
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
