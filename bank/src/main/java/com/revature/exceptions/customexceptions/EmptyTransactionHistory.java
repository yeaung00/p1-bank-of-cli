package com.revature.exceptions.customexceptions;

import com.revature.exceptions.RepositoryException;

public class EmptyTransactionHistory extends RepositoryException {
    public EmptyTransactionHistory(String message) {
        super(message);
    }

    public EmptyTransactionHistory(String message, Throwable cause) {
        super(message, cause);
    }
}
