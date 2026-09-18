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
    @DisplayName("getBalance returns true for an existing account")
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

    @Test
    @DisplayName("getBalance returns false for a non-existing account")
    void testGetBalanceNonExisting() {
        assertThrows(
                AccountNotFoundException.class,
                () -> Repository.getBalance("NonExistent")
        );
    }

    @Test
    void testValidUpdateBalance() throws RepositoryException {
        BigDecimal amountChangedBy = new BigDecimal("102.45").subtract(Repository.getBalance("Billy"));
        int result = Repository.updateBalance("Billy", new BigDecimal("102.45"), "DEPOSIT", amountChangedBy);
        BigDecimal balance = Repository.getBalance("Billy");
        Assertions.assertEquals(1, result);
        Assertions.assertEquals(new BigDecimal("102.45"), balance);
    }

    @Test
    void testInvalidUpdateBalance() throws RepositoryException {
        Assertions.assertThrows(RepositoryException.class, () -> {Repository.updateBalance("UserDoesntExist", new BigDecimal("102.45"), "DEPOSIT", BigDecimal.ZERO);});
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

    @Test
    @DisplayName("transfer debits the sender and credits the recipient")
    void testValidTransfer() throws RepositoryException {
        Repository.transfer("Billy", "Sally", new BigDecimal("0.25"));
        assertEquals(0, Repository.getBalance("Billy").compareTo(new BigDecimal("0.75")));
        assertEquals(0, Repository.getBalance("Sally").compareTo(new BigDecimal("0.75")));
    }

    @Test
    @DisplayName("transfer writes a TRANSFER_OUT and TRANSFER_IN record")
    void testTransferWritesTransactionHistory() throws Exception {
        Repository.transfer("Billy", "Sally", new BigDecimal("0.25"));

        ArrayList<Transaction> billyHistory = Repository.getTransactionHistory("Billy");
        Transaction billyTransfer = billyHistory.get(billyHistory.size() - 1);
        assertEquals("TRANSFER_OUT", billyTransfer.getType());
        assertEquals(0, billyTransfer.getAmount().compareTo(new BigDecimal("0.25")));
        assertEquals("Sally", billyTransfer.getRelatedAccountId());

        ArrayList<Transaction> sallyHistory = Repository.getTransactionHistory("Sally");
        Transaction sallyTransfer = sallyHistory.get(sallyHistory.size() - 1);
        assertEquals("TRANSFER_IN", sallyTransfer.getType());
        assertEquals(0, sallyTransfer.getAmount().compareTo(new BigDecimal("0.25")));
        assertEquals("Billy", sallyTransfer.getRelatedAccountId());
    }

    @Test
    @DisplayName("transfer throws and changes nothing when the sender lacks funds")
    void testTransferInsufficientFunds() throws RepositoryException {
        assertThrows(
                TransactionFailedException.class,
                () -> Repository.transfer("Billy", "Sally", new BigDecimal("5.00"))
        );

        assertEquals(0, Repository.getBalance("Billy").compareTo(new BigDecimal("1.00")));
        assertEquals(0, Repository.getBalance("Sally").compareTo(new BigDecimal("0.50")));
    }

    @Test
    @DisplayName("transfer rolls back the debit when the recipient does not exist")
    void testTransferRollsBackForMissingRecipient() throws RepositoryException {
        assertThrows(
                AccountNotFoundException.class,
                () -> Repository.transfer("Billy", "NonExistent", new BigDecimal("0.25"))
        );

        assertEquals(0, Repository.getBalance("Billy").compareTo(new BigDecimal("1.00")));
    }

    @Test
    @DisplayName("transfer throws when the sender does not exist")
    void testTransferMissingSender() {
        assertThrows(
                TransactionFailedException.class,
                () -> Repository.transfer("NonExistent", "Sally", new BigDecimal("0.25"))
        );
    }
}
