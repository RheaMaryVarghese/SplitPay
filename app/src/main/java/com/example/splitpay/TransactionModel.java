package com.example.splitpay;

public class TransactionModel {
    private String userName;
    private float amount;

    public TransactionModel(String userName, float amount) {
        this.userName = userName;
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public float getAmount() {
        return amount;
    }
}
