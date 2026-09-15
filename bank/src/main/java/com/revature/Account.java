package com.revature;

import java.time.LocalDateTime;

public class Account {
    private String accountId;
    private String pin;
    private double balance;
    private LocalDateTime creationDate;

    // This constructor will be used when creating a new account
    public Account(String accoundId, String pin) {
        this(accoundId, pin, 0.0);
    }

    // This constructor will be used when logging into an existing account
    public Account(String accountId, String pin, double balance) {
        // redundant check that arguments are valid
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException(
                    "Account Id cannot be blank"
            );
        }

        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException(
                    "PIN cannot be blank"
            );
        }

        this.accountId = accountId;
        this.pin = pin;
        this.balance = balance;
        this.creationDate = LocalDateTime.now();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreationDate() {
        return creationDate.toString();
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
