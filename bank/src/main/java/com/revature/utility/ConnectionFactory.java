package com.revature.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    // avoiding handling the error here and propagating it back up
    public static Connection getAutoCommitConnect() throws SQLException {
        checkDatabasePath();
        Connection connection = DriverManager.getConnection(getDatabaseUrl());
        connection.setAutoCommit(true);
        return connection;
    }

    public static Connection getManualCommitConnection() throws SQLException {
        checkDatabasePath();
        Connection connection = DriverManager.getConnection(getDatabaseUrl());
        connection.setAutoCommit(false);
        configureForeignKeyEnforcement(connection);
        return connection;
    }

    private static void configureForeignKeyEnforcement(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "PRAGMA foreign_keys = true";
            statement.execute(sql);
        }
    }

    // checks that the user properly configured the BANK_DATABASE_PATH environment variable
    private static void checkDatabasePath() {
        if (getDatabaseUrl() == null || getDatabaseUrl().isBlank()) {
            throw new IllegalStateException(
                    "BANK_DATABASE_PATH environment variable is not configured"
            );
        }
    }

    /*
        This function attempts to get the database URL from either the system properties (for testing) or the
        system environment variable (for default application purposes)
     */
    private static String getDatabaseUrl() {
        // this property is controlled by the Java program, therefore it is usually used for testing
        String databasePath = System.getProperty("bank.database.path");

        if  (databasePath == null || databasePath.isBlank()) {
            // if system property not available, tries to get environment variable
            databasePath = System.getenv("BANK_DATABASE_PATH");
        }

        if  (databasePath == null || databasePath.isBlank()) {
            throw new IllegalStateException("Database path environment variable is not configured");
        }

        return "jdbc:sqlite:" + databasePath;
    }
}
