package com.revature;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;
import com.revature.utility.ConnectionFactory;
import com.revature.utility.MoneyUtils;

public class RepositoryTest {

    @BeforeEach
    void createDummyInstance() throws SQLException {
        String sqlQuery = "insert into accounts (account_id, pin_hash, balance_cents) values (?, ?, ?)";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            ps.setString(1, "TestUser");
            ps.setString(2, "Test");
            ps.setInt(3, 10145);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testValidUpdateBalance() throws RepositoryException {
        int result = Repository.updateBalance("TestUser", new BigDecimal("102.45"));
        BigDecimal balance = Repository.getBalance("TestUser");
        Assertions.assertEquals(new BigDecimal("102.45"), balance);
        Assertions.assertEquals(1, result);
    }

    @Test 
    void testInvalidUpdateBalance() throws RepositoryException {
        Assertions.assertThrows(RepositoryException.class, () -> {Repository.updateBalance("UserDoesntExist", new BigDecimal("102.45"));});
    }

    @AfterEach 
    void clearDummyInstance() {
        String sqlQuery = "delete from accounts where account_id = ?";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            ps.setString(1, "TestUser");
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeAll
    static void setupDatabase() throws Exception {
        TestDatabaseHelper.setupDatabase();
    }

    @BeforeEach
    void populateDatabase() throws Exception {
        TestDatabaseHelper.populateDatabase();
    }

    @Test
    void testGetBalance() throws AccountNotFoundException, DatabaseException {
        BigDecimal balance = Repository.getBalance("Billy");

        assertEquals(0, balance.compareTo(new BigDecimal("1.00")));
    }

    @AfterEach
    void clearDatabase() throws Exception {
        TestDatabaseHelper.clearDatabase();
    }
}
