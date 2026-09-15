package com.revature.exceptions.customexceptions;

import com.revature.exceptions.RepositoryException;

public class DatabaseException extends RepositoryException {
    public DatabaseException(String message) {
        super(message);
    }
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
