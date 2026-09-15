package com.revature;

public class Account {
    private int accountId;
    private String pin;
    private double balance;
    private String creationDate;

    public Account(int accountId, String pin, double balance, String creationDate) {
        // redundant check that arguments are valid
        if (accountId <= 0) {
            throw new IllegalArgumentException(
                    "Account ID cannot be zero or negative"
            );
        }

        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException(
                    "PIN cannot be blank"
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

    public Account(int accountId, String pin) {
        this(accountId, pin, 0.0, null);
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
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
