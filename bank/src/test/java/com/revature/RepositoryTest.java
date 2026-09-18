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

    @Test
    void testValidUpdateBalance() throws RepositoryException {
        int result = Repository.updateBalance("Billy", new BigDecimal("102.45"));
        BigDecimal balance = Repository.getBalance("Billy");
        Assertions.assertEquals(1, result);
        Assertions.assertEquals(new BigDecimal("102.45"), balance);
    }

    @Test 
    void testInvalidUpdateBalance() throws RepositoryException {
        Assertions.assertThrows(RepositoryException.class, () -> {Repository.updateBalance("UserDoesntExist", new BigDecimal("102.45"));});
    }

    @AfterEach
    void clearDatabase() throws Exception {
        TestDatabaseHelper.clearDatabase();
    }
}
