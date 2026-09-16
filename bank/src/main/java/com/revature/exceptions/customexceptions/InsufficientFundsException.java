package com.revature.exceptions.customexceptions;

import com.revature.exceptions.BusinessException;

public class InsufficientFundsException extends BusinessException {
    public InsufficientFundsException(String message) {
        super(message);
    }
    public InsufficientFundsException(String message, Throwable cause) {
        super(message,cause);
    }
}
