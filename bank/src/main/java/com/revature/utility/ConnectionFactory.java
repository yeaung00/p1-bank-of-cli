package com.revature.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    public static final String DATABASE_PATH = System.getenv("BANK_DATABASE_PATH");
    public static final String URL = "jdbc:sqlite:" + DATABASE_PATH;

    // avoiding handling the error here and propagating it back up
    public static Connection getAutoCommitConnect() throws SQLException {
        checkDatabasePath();
        Connection connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(true);
        return connection;
    }

    public static Connection getManualCommitConnection() throws SQLException {
        checkDatabasePath();
        Connection connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        configureForeignKeyEnforcement(connection);
        return connection;
    }

    public static void configureForeignKeyEnforcement(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "PRAGMA foreign_keys = true";
            statement.execute(sql);
        }
    }

    // checks that the user properly configured the BANK_DATABASE_PATH environment variable
    public static void checkDatabasePath() {
        if (DATABASE_PATH == null || DATABASE_PATH.isBlank()) {
            throw new IllegalStateException(
                    "BANK_DATABASE_PATH environment variable is not configured"
            );
        }
    }

    /*
        Generally speaking we should have at least two tables:
            - Accounts      -> to keep all relevant account information (accountId, password, balance,
            date time for logging?)
            - Transactions  -> to keep track of all transactions (transactionId, accountId (initiator),
            type of transaction, amount, other account (if applicable), date time for logging?)
     */
}
