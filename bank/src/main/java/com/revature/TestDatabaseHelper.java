package com.revature;

import com.revature.exceptions.customexceptions.DatabaseException;
import com.revature.utility.ConnectionFactory;
import com.revature.utility.DatabaseInitializer;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseHelper {
    private static final String TEST_DATABASE_PATH = "data/testBank.db";

    public static void setupDatabase() throws Exception {
        System.setProperty("bank.database.path", Paths.get(TEST_DATABASE_PATH).toAbsolutePath().normalize().toString());

        DatabaseInitializer.initialize();
    }

    public static void populateDatabase() throws DatabaseException {
        String query = "INSERT INTO accounts (account_id, pin_hash, balance_cents) VALUES " +
                "(\"Billy\", \"1\", 100)," +
                "(\"Sally\", \"abc\", 50)," +
                "(\"Damon\", \"7\", 0)," +
                "(\"Slagathor\", \"time\", 3000)," +
                "(\"Timmy\", \"hello\", 200);";

        try (
                Connection conn = ConnectionFactory.getAutoCommitConnect();
                Statement stmt = conn.createStatement();
                ) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to populate tables", e);
        }
    }

    public static void clearDatabase() throws DatabaseException {
        try (
                Connection conn = ConnectionFactory.getAutoCommitConnect();
                Statement stmt = conn.createStatement();
            ) {
            stmt.executeUpdate("DELETE FROM transactions");
            stmt.executeUpdate("DELETE FROM accounts");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to clear tables", e);
        }
    }

    public static void deleteDatabase() throws DatabaseException {
        File database = new File(TEST_DATABASE_PATH);

        if (database.exists()) {
            database.delete();
        }
    }
}
