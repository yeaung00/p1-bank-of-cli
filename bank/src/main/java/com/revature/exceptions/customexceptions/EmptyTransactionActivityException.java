package com.revature.exceptions.customexceptions;

import com.revature.exceptions.BusinessException;

public class EmptyTransactionActivityException extends BusinessException {
    public EmptyTransactionActivityException(String message) {
        super(message);
    }
}
