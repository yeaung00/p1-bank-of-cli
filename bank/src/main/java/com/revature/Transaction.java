package com.revature;

import java.math.BigDecimal;

public class Transaction {

    //Initializing entity fields
    private String transactionId;
    private String accountId;
    private String type;
    // Saving amount to double, subject to change based on Damon/Ye
    private BigDecimal amount;
    private String relatedAccountId;
    private String creationDate;

    //Constructor

    public Transaction(String transactionId, String accountId, String type, BigDecimal amount, String relatedAccountId) {

        if(transactionId == null || transactionId.isEmpty()){
            throw new IllegalArgumentException("Transaction ID cannot be empty.");
        }
        if(accountId == null || accountId.isEmpty()){
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }
        if(type == null || type.isEmpty()){
            throw new IllegalArgumentException("Transaction must have a valid type.");
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0 ){
            throw new IllegalArgumentException("Cannot transfer negative amount.");
        }
        if(relatedAccountId == null || relatedAccountId.isEmpty()){
            throw new IllegalArgumentException("Transaction recipient cannot be blank");
        }

        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.relatedAccountId = relatedAccountId;
    }

    public Transaction(String transactionId, String accountId, String type, BigDecimal amount, String relatedAccountId, String creationDate){
        this(transactionId, accountId, type, amount, relatedAccountId);
        this.creationDate = creationDate;

    }


    //Get/Setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRelatedAccountId() {
        return relatedAccountId;
    }

    public void setRelatedAccountId(String relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }

    public String getCreationDate(){
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}

