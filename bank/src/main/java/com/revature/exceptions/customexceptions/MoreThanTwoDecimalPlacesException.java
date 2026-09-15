package com.revature.exceptions.customexceptions;

import com.revature.exceptions.BusinessException;

public class MoreThanTwoDecimalPlacesException extends BusinessException {
    public MoreThanTwoDecimalPlacesException(String message) {
        super(message);
    }
}
