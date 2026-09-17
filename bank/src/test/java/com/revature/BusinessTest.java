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

    // this test entirely depends on the state of your database, I'm currently working on normalizing the test
    // database to keep everything consistent
    @Test
    void testViewBalance() throws BankException {
        BigDecimal expectedBalance = new BigDecimal(0);

        try (MockedStatic<Repository> repo = Mockito.mockStatic(Repository.class)) {
            repo.when(() -> Repository.getBalance("Billy")).thenReturn(expectedBalance);
        }

        BigDecimal actualBalance = Business.viewBalance("Billy");
        assertEquals(0, actualBalance.compareTo(expectedBalance));
    }
}
