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
        createTables();
        createDirectories();
    }

    private static void createTables() throws SQLException {
        Connection conn = ConnectionFactory.getAutoCommitConnect();

        Statement stmt = conn.createStatement();
        String tableSQL = "CREATE TABLE IF NOT EXISTS accounts (\n" +
                "    account_id TEXT PRIMARY KEY,\n" +
                "    pin_hash TEXT NOT NULL,\n" +
                "    balance_cents INTEGER NOT NULL DEFAULT 0 CHECK (balance_cents >= 0),\n" +
                "    creationDate TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS transactions (\n" +
                "    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    account_id TEXT NOT NULL,\n" +
                "    transaction_type TEXT NOT NULL CHECK (\n" +
                "        transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT')\n" +
                "    ),\n" +
                "    amount_cents INTEGER NOT NULL CHECK (amount_cents > 0),\n" +
                "    related_account_id TEXT,\n" +
                "    creationDate TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    FOREIGN KEY (account_id) REFERENCES accounts(account_id)\n" +
                ");\n" +
                "\n" +
                "CREATE INDEX IF NOT EXISTS idx_transactions_account_id\n" +
                "    ON transactions(account_id);";
        stmt.execute(tableSQL);
    }

    // would create a data folder at the root directory (bank) if it does not exist to ensure
    // the database path would be correct?
    private static void createDirectories() throws IOException {
        // this gets the path to DatabaseInitializer.java
        Path workingDir = Paths.get("").toAbsolutePath();
        // this gets the path to the bank directory
        Path projectRoot = workingDir.resolve("bank");
        // this attempts to check if data is available
        Path dataDir = projectRoot.resolve("data");

        Files.createDirectories(dataDir);
    }
}
