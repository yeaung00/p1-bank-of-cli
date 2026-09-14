package com.revature.exceptions.customexceptions;

import com.revature.exceptions.BusinessException;

public class NegativeInputException extends BusinessException {
    public NegativeInputException(String message) {
        super(message);
    }
}
