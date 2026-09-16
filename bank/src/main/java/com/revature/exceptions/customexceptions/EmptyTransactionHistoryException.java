package com.revature.exceptions.customexceptions;

import com.revature.exceptions.RepositoryException;

public class EmptyTransactionHistoryException extends RepositoryException {
    public EmptyTransactionHistoryException(String message) {
        super(message);
    }

    public EmptyTransactionHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
