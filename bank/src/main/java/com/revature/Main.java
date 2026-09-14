package com.revature;

import com.revature.utility.DatabaseInitializer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // possibly change based on where we want to handle the exceptions
        try {
            DatabaseInitializer.initialize();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }

        API theAPI = new API();
        theAPI.run();
    }
}
