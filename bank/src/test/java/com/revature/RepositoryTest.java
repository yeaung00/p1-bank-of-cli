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
    @DisplayName ("Tests whether you can retrieve a valid Account from the database")
    void testValidGetAccount() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        try (MockedStatic<ConnectionFactory> connectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            connectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
            Mockito.when(statement.executeQuery()).thenReturn(resultSet);
            Mockito.when(resultSet.next()).thenReturn(true);
            Mockito.when(resultSet.getString("account_id")).thenReturn("account-123");
            Mockito.when(resultSet.getString("pin_hash")).thenReturn("1234");
            Account account = assertDoesNotThrow(() -> Repository.getAccount("account-123", "1234"));
            assertEquals("account-123", account.getAccountId());
            assertEquals("1234", account.getPin());
            Mockito.verify(statement).setString(1, "account-123");
            Mockito.verify(statement).setString(2, "1234");
        }
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException for invalid account credentials")
    void testInvalidGetAccount() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement accountQuery = Mockito.mock(PreparedStatement.class);
        PreparedStatement accountIdQuery = Mockito.mock(PreparedStatement.class);
        PreparedStatement pinQuery = Mockito.mock(PreparedStatement.class);
        ResultSet accountResult = Mockito.mock(ResultSet.class);
        ResultSet accountIdResult = Mockito.mock(ResultSet.class);
        ResultSet pinResult = Mockito.mock(ResultSet.class);

        try (MockedStatic<ConnectionFactory> connectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            connectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(accountQuery, accountIdQuery, pinQuery);
            Mockito.when(accountQuery.executeQuery()).thenReturn(accountResult);
            Mockito.when(accountIdQuery.executeQuery()).thenReturn(accountIdResult);
            Mockito.when(pinQuery.executeQuery()).thenReturn(pinResult);
            Mockito.when(accountResult.next()).thenReturn(false);
            Mockito.when(accountIdResult.next()).thenReturn(false);
            Mockito.when(pinResult.next()).thenReturn(false);
            AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,() -> Repository.getAccount("missing-account", "9999"));
            assertTrue(exception.getMessage().contains("missing-account"));
            assertTrue(exception.getMessage().contains("9999"));
            Mockito.verify(accountQuery).setString(1, "missing-account");
            Mockito.verify(accountQuery).setString(2, "9999");
        }
    }
}
