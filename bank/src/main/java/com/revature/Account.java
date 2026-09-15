package com.revature;

public class Account {
    private String accountId;
    private String pin;
    private double balance;
    private String creationDate;

    public Account(String accountId, String pin, double balance, String creationDate) {
        // redundant check that arguments are valid
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException(
                    "Account ID cannot be null or empty"
            );
        }

        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException(
                    "PIN cannot be null or empty"
            );
        }

        if (balance < 0) {
            throw new IllegalArgumentException(
                    "Balance cannot be negative"
            );
        }

        this.accountId = accountId;
        this.pin = pin;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public Account(String accountId, String pin) {
        this(accountId, pin, 0.0, null);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        // redundant check if balance is valid
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        this.balance = balance;
    }
}
