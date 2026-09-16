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
    @DisplayName("verifyCredentials returns true for valid credentials")
    void testValidVerifyCredentials() throws Exception {
        String accountId = "Billy";
        String pin = "1234";
        Account account = new Account(accountId, pin);
        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.getAccount(accountId, pin)).thenReturn(account);
            boolean result = Business.verifyCredentials(accountId, pin);
            assertTrue(result);
            mockRepository.verify(() -> Repository.getAccount(accountId, pin));
        }
    }
    @Test
    void testInvalidCredentials() {
        String accountId = "Billy";
        String pin = "wrong";
        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.getAccount(accountId, pin)).thenThrow(new AccountNotFoundException("Account not found"));
            assertThrows(
                // Remember BusinessException is an abstract supertype which means
                // we can catch any business layer error
                BusinessException.class,
                () -> Business.verifyCredentials(accountId, pin)
            );
        }
    }
}
