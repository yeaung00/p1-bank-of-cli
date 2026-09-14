package com.revature;

import java.sql.Connection;
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
    private static void getBalance() {

    }

    // (updateBalance) Updates the value of balance during deposits and withdraws - Connor
    public static void updateBalance(String accountID, double amount) throws SQLException {
        /*
        String sqlQuery = "UPDATE accounts SET balance = ? where accountID = ?";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            ps.setDouble(1, amount);
            ps.setString(2, accountID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Deposit failed.");
            }
        }
        */
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
