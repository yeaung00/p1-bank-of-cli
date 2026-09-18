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

    @AfterEach
    void clearDatabase() throws Exception {
        TestDatabaseHelper.clearDatabase();
    }

    @Test
    void testGetBalance() throws AccountNotFoundException, DatabaseException {
        BigDecimal balance = Repository.getBalance("Billy");

        assertEquals(0, balance.compareTo(new BigDecimal("1.00")));
    }

    @Test 
    @DisplayName ("Should return a non-empty list of transactions")
    void testValidGetTransactionHistory() throws Exception {
        ArrayList<Transaction> transactions = Repository.getTransactionHistory("Billy");

        assertEquals(2, transactions.size());
        assertEquals("Billy", transactions.get(0).getAccountId());
        assertEquals("DEPOSIT", transactions.get(0).getType());
        assertEquals(new BigDecimal("1.00"), transactions.get(0).getAmount());
        assertEquals("Billy", transactions.get(0).getRelatedAccountId());
        assertEquals("TRANSFER_OUT", transactions.get(1).getType());
        assertEquals(new BigDecimal("0.25"), transactions.get(1).getAmount());
        assertEquals("Sally", transactions.get(1).getRelatedAccountId());
    }

    @Test 
    @DisplayName ("Should throw EmptyTransactionHistoryException error")
    void testInvalidGetTransactionHistory() throws BankException {
        assertThrows(
                EmptyTransactionHistoryException.class,
                () -> Repository.getTransactionHistory("missing-account")
        );
    }
}
