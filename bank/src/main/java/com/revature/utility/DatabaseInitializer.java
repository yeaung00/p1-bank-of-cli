package com.revature.utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class DatabaseInitializer {
    /*
        What I imagine this should do:
            - should be run in the Main.java application
            - should create necessary directories (assuming that there is no data folder at the specified path)
            - create the tables
            - configures sqlite (in the connectionFactory)
            - possibly run a schema set up?
     */

    public static void initialize() throws SQLException, IOException {
        createDirectories();
        createTables();
    }

    private static void createTables() throws SQLException {
        Connection conn = ConnectionFactory.getAutoCommitConnect();

        Statement stmt = conn.createStatement();
        String accountTableSQL = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_id TEXT PRIMARY KEY," +
                "pin_hash TEXT NOT NULL," +
                "balance_cents INTEGER NOT NULL DEFAULT 0 CHECK (balance_cents >= 0)," +
                "creationDate TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ");";
        String transactionTableSQL =
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account_id TEXT NOT NULL," +
                "transaction_type TEXT NOT NULL CHECK (" +
                "transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT')" +
                ")," +
                "amount_cents INTEGER NOT NULL CHECK (amount_cents > 0)," +
                "related_account_id TEXT," +
                "creationDate TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (account_id) REFERENCES accounts(account_id)" +
                ");";
        stmt.execute(accountTableSQL);
        stmt.execute(transactionTableSQL);
    }

    // would go down to the root directory of the project (p1-bank-of-cli) and check whether a data directory exists
    // in the bank folder (p1-bank-of-cli/bank/data), creating one for the BANK_DATABASE_PATH to be valid
    private static void createDirectories() throws IOException {
        Path currentPath = Paths.get("").toAbsolutePath().normalize();
        Path projectRoot = currentPath;

        // Search upward for the p1-bank-of-cli project directory
        while (projectRoot != null
                && !projectRoot.getFileName().toString().equals("p1-bank-of-cli")) {
            projectRoot = projectRoot.getParent();
        }

        if (projectRoot == null) {
            throw new IOException(
                    "Could not find the p1-bank-of-cli project directory."
            );
        }

        Path dataDirectory = projectRoot.resolve("bank").resolve("data");

        // Creates bank and data if either does not exist.
        // Does nothing if data already exists as a directory.
        Files.createDirectories(dataDirectory);
    }
}
