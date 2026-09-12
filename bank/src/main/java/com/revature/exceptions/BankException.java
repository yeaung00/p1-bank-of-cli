package com.revature.exceptions;

public abstract class BankException extends Exception{
    public BankException(String message) {
        super(message);
    }

    //overloaded constructor, cause param helps to print stack trace for debugging
    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}


