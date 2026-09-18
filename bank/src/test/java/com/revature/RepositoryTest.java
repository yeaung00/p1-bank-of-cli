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
    @DisplayName ("Tests whether you can retrieve a valid Account from the database")
    void testValidGetAccount() throws BusinessException, RepositoryException{
        Account account = Repository.getAccount("Billy", "1");

        assertEquals("Billy", account.getAccountId());
        assertEquals("1", account.getPin());
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException for invalid account credentials")
    void testInvalidGetAccount() {
        assertThrows(AccountNotFoundException.class, () -> Repository.getAccount("Billy", "9999"));
    }
}
