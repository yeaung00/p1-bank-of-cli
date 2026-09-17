package com.revature;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.revature.exceptions.*;
import com.revature.exceptions.customexceptions.*;

public class BusinessTest {

    @Test
    void testValidDeposit() throws BusinessException, RepositoryException {
        try (MockedStatic<Repository> mockRepo = Mockito.mockStatic(Repository.class);
        ) {
            mockRepo.when(() -> Repository.updateBalance("Billy", new BigDecimal("800"))).thenReturn(1);
            mockRepo.when(() -> Repository.getBalance("Billy")).thenReturn(BigDecimal.ZERO);
            boolean result = Business.validDeposit("Billy", new BigDecimal("800"));
            Assertions.assertEquals(true, result);
        }
    }

    // Tests that if a negative amount is entered, a BusinessException is thrown.
    @Test
    void testNegativeDeposit() throws BusinessException, RepositoryException {
        Assertions.assertThrows(BusinessException.class, () -> {Business.validDeposit("Billy", new BigDecimal(-800));});
    }

    // Tests that if an amount with more than two decimal places is entered, a BusinessException is thrown.
    @Test
    void testMoreThanTwoDecimalPlacesDeposit() {
        Assertions.assertThrows(BusinessException.class, () -> {Business.validDeposit("Billy", new BigDecimal(100.401));});
    }
}
