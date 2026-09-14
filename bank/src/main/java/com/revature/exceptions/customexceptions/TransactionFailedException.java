package com.revature.exceptions.customexceptions;

import com.revature.exceptions.RepositoryException;

public class TransactionFailedException extends RepositoryException {
    public TransactionFailedException(String message) {
        super(message);
    }
}
