package com.revature;

public class Account {
    private String accountID;
    private String pin;
    private double balance;

    public Account(){}

    public Account(String accountID, String pin, double balance) {
        this.accountID = accountID;
        this.pin = pin;
        this.balance = balance;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
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
        // System.out.println("This would call the repo layer to update that in the DB");
        this.balance = balance;
    }
}
