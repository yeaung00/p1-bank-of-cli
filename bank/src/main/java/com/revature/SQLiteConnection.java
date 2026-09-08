package com.revature;

import java.sql.*;

public class SQLiteConnection {
    private static final String URL = "jdbc:sqlite:data/bank.db";

    public static Connection open() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        try (Statement statement = conn.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }
}
