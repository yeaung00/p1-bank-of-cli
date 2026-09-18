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

    @AfterEach
    void clearDatabase() throws Exception {
        TestDatabaseHelper.clearDatabase();
    }


    //-----------------------------Ydur tests-----------------
    @Test
    void addAccountPos() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);

        try (MockedStatic<ConnectionFactory> mockConnectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            mockConnectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);

            Repository.addAccount("Billy", "1234");

            Mockito.verify(statement).setString(1, "Billy");
            Mockito.verify(statement).setString(2, "1234");
            Mockito.verify(statement).setString(3, "0");
            Mockito.verify(statement).executeUpdate();
        }
    }

    @Test
    void addAccountNeg() throws Exception {
        Connection connection = Mockito.mock(Connection.class);

        try (MockedStatic<ConnectionFactory> mockConnectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            mockConnectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());

            assertThrows(DatabaseException.class, () -> Repository.addAccount("Billy", "1234")
            );
        }
    }

    @Test
    void checkExistingAccountPos() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet result = Mockito.mock(ResultSet.class);

        try (MockedStatic<ConnectionFactory> mockConnectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            mockConnectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
            Mockito.when(statement.executeQuery()).thenReturn(result);
            Mockito.when(result.next()).thenReturn(true);

            boolean exists = Repository.checkExistingAccounts("Billy");

            assertTrue(exists);
        }
    }

    @Test
    void checkExistingAccountNeg() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet result = Mockito.mock(ResultSet.class);

        try (MockedStatic<ConnectionFactory> mockConnectionFactory = Mockito.mockStatic(ConnectionFactory.class)) {
            mockConnectionFactory.when(() -> ConnectionFactory.getAutoCommitConnect()).thenReturn(connection);
            Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(statement);
            Mockito.when(statement.executeQuery()).thenReturn(result);
            Mockito.when(result.next()).thenReturn(false);

            boolean exists = Repository.checkExistingAccounts("Billy");

            assertFalse(exists);
        }
    }
}
