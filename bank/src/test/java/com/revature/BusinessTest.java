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

}
