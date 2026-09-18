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
    @DisplayName ("validateTransactionHistory should return list of Transactions")
    void testSuccessfulValidateTransactionHistory() throws BankException{
        //ARRANGE: we set up the variables/objects needed in order to test the method
        String accountId = "Billy";
        String pin = "1234";
        Account user = new Account(accountId, pin);
        ArrayList<Transaction> out = new ArrayList<>();
        out.add(new Transaction("placeholder", "Billy", "WITHDRAWAL", BigDecimal.valueOf(9999), "Billy"));
        //create mockito obj mocking repo layer
        try(MockedStatic<Repository> mockRepo = Mockito.mockStatic(Repository.class)) {
            //whenever these two methods get called, then return specified dummy data
            mockRepo.when(() -> Repository.getAccount(accountId, pin)).thenReturn(user);
            mockRepo.when(() -> Repository.getTransactionHistory(accountId)).thenReturn(out);

            // ACT: we call the method we are testing
            ArrayList<Transaction> result = Business.validateTransactionHistory(accountId, pin);
            //ASSERT: we check whether the result is how we intend it to be 
            assertEquals(out,result);
            mockRepo.verify(() -> Repository.getAccount(accountId, pin));
            mockRepo.verify(() -> Repository.getTransactionHistory(accountId));
        }
    }

    @Test
    @DisplayName("validateTransactionHistory throws for an unsuccessful validation")
    void testUnsuccessfulValidateTransactionHistory() throws BankException{
        String accountId = "Billy";
        String pin = "1234";

        try (MockedStatic<Repository> mockRepo = Mockito.mockStatic(Repository.class)) {
            mockRepo.when(() -> Repository.getAccount(accountId, pin))
                    .thenReturn(new Account(accountId, pin));
            mockRepo.when(() -> Repository.getTransactionHistory(accountId))
                    .thenThrow(new EmptyTransactionHistoryException("No transaction history found"));

            assertThrows(
                    EmptyTransactionHistoryException.class,
                    () -> Business.validateTransactionHistory(accountId, pin)
            );
        }
    }

    @Test
    @DisplayName("verifyCredentials returns true for valid credentials")
    void testValidVerifyCredentials() throws BankException {
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
    @DisplayName("verifyCredentials throws for invalid credentials")
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
//============================================Ydur's Tests===================

    @Test
    void verifyRegistrationPos() throws InvalidCredentialsException {
        String accountId = "Billy";
        String pin = "1234";

        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.checkExistingAccounts(accountId)).thenReturn(false);

            boolean result = Business.verifyRegistration(accountId, pin);

            assertTrue(result);

            mockRepository.verify(() -> Repository.checkExistingAccounts(accountId));
            mockRepository.verify(() -> Repository.addAccount(accountId, pin));
        }
    }

    @Test
    void verifyRegistrationNeg() throws InvalidCredentialsException {
        String accountId = "Billy";
        String pin = "1234";

        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.checkExistingAccounts(accountId)).thenReturn(true);

            boolean result = Business.verifyRegistration(accountId, pin);

            assertFalse(result);

            mockRepository.verify(() -> Repository.checkExistingAccounts(accountId));
            mockRepository.verify(() -> Repository.addAccount(accountId, pin), Mockito.never());
        }
    }

    @Test
    void validWithdrawPos() throws Exception {
        String accountId = "Billy";
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal amount = new BigDecimal("25.00");

        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.getBalance(accountId)).thenReturn(balance);
            mockRepository.when(() -> Repository.updateBalance(accountId, new BigDecimal("75.00"))).thenReturn(1);

            boolean result = Business.validWithdraw(accountId, amount);

            assertTrue(result);

            mockRepository.verify(() -> Repository.getBalance(accountId), Mockito.times(2));
            mockRepository.verify(() -> Repository.updateBalance(accountId, new BigDecimal("75.00")));
        }
    }

    @Test
    void validWithdrawNeg() throws Exception {
        String accountId = "Billy";
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal amount = new BigDecimal("150.00");

        try (MockedStatic<Repository> mockRepository = Mockito.mockStatic(Repository.class)) {
            mockRepository.when(() -> Repository.getBalance(accountId)).thenReturn(balance);

            assertThrows(
                    InsufficientFundsException.class,
                    () -> Business.validWithdraw(accountId, amount)
            );

            mockRepository.verify(() -> Repository.getBalance(accountId));
            mockRepository.verify(() -> Repository.updateBalance(Mockito.anyString(), Mockito.any(BigDecimal.class)),
                    Mockito.never()
            );
        }
    }
}
