package com.revature.exceptions.customexceptions;

import com.revature.exceptions.RepositoryException;

public class AccountNotFoundException extends RepositoryException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
