package com.revature.exceptions.customexceptions;

import com.revature.exceptions.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
