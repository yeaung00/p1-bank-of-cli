package com.revature;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

//we will create mock db using mockito
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;
import com.revature.utility.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

//NOTE: this is just introducing JUnit testing. 
// DO NOT add any more test files
// until we finish the full original structure in main
// because our test structure must match main structure

// to run tests, use command mvn test from bank dir
public class APITest {

    private API theAPI;

    @BeforeEach 
    void setup() {
        theAPI = new API();
    }

    @Test 
    @DisplayName ("API object should not be null")
    void testNullAPIObject() {
        Assertions.assertNotNull(theAPI);
    }

    // Integration - Don't run on this file.
    /*
    @Test
    void createDummyInstance() throws SQLException {
        String sqlQuery = "insert into accounts (account_id, pin_hash, balance_cents) values (?, ?, ?)";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            ps.setString(1, "Billy");
            ps.setString(2, "4");
            ps.setInt(3, 10145);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    // Tests if the validDeposit function in the Business layer works
    @Test
    void testValidDeposit() throws BusinessException, RepositoryException {
        try (MockedStatic<Repository> mockRepo = Mockito.mockStatic(Repository.class)) {
            mockRepo.when(() -> Repository.updateBalance("Billy", 800)).thenReturn(1);
            boolean result = Business.validDeposit("Billy", 800);
            Assertions.assertEquals(true, result);
        }
    }

    // Tests that if a negative amount is entered, a BusinessException is thrown.
    @Test
    void testNegativeDeposit() throws BusinessException, RepositoryException {
        Assertions.assertThrows(BusinessException.class, () -> {Business.validDeposit("Billy", -800);});
    }

    // Tests that if an amount with more than two decimal places is entered, a BusinessException is thrown.
    @Test
    void testMoreThanTwoDecimalPlacesDeposit() {
        Assertions.assertThrows(BusinessException.class, () -> {Business.validDeposit("Billy", 100.401);});
    }

    */

    // For integration tests, don't run here
    /*
    @AfterEach 
    void clearDummyInstance() {
        String sqlQuery = "delete from accounts where account_id = ?";
        try (
            Connection connection = ConnectionFactory.getAutoCommitConnect();
            PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ) {
            ps.setString(1, "Billy");
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */
}
