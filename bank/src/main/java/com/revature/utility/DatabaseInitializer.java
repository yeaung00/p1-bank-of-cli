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

    // would create a data folder at the root directory (bank) if it does not exist to ensure
    // the database path would be correct?
    private static void createDirectories() throws IOException {
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path dataDir = projectRoot.resolve("data");

        Files.createDirectories(dataDir);
    }
}
