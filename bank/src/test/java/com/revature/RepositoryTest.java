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
    @Test 
    @DisplayName ("Should return a non-empty list of transactions")
    void testValidGetTransactionHistory() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("account_id")).thenReturn("Billy");
        Mockito.when(resultSet.getString("transaction_type")).thenReturn("DEPOSIT");
        Mockito.when(resultSet.getInt("amount_cents")).thenReturn(1250);
        Mockito.when(resultSet.getString("related_account_id")).thenReturn("Billy");
        Mockito.when(resultSet.getString("creationDate")).thenReturn("blaaaablaa");
        try (MockedStatic<ConnectionFactory> connectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            connectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            ArrayList<Transaction> transactions = Repository.getTransactionHistory("Billy");
            assertEquals(1, transactions.size());
            Transaction transaction = transactions.get(0);
            assertEquals("Billy", transaction.getAccountId());
            assertEquals("DEPOSIT", transaction.getType());
            assertEquals(new BigDecimal("12.50"), transaction.getAmount());
            assertEquals("Billy", transaction.getRelatedAccountId());
            assertEquals("blaaaablaa", transaction.getCreationDate());
        }
    }

    @Test 
    @DisplayName ("Should throw EmptyTransactionHistoryException error")
    void testInvalidGetTransactionHistory() throws BankException, SQLException {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);
        try (MockedStatic<ConnectionFactory> connectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            connectionFactory.when(ConnectionFactory::getAutoCommitConnect).thenReturn(connection);
            assertThrows(EmptyTransactionHistoryException.class, () -> Repository.getTransactionHistory("missing-account"));
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
