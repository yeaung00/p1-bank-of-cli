package com.revature;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//we will create mock db using mockito
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;
import java.sql.SQLException;

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
}
