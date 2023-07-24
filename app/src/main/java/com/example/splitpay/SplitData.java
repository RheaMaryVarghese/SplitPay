package com.example.splitpay;

import java.util.List;

public class SplitData {
    private String user_name;
    private Float amount_to_pay;

    public SplitData(String user_name, Float amount_to_pay) {
        this.user_name = user_name;
        this.amount_to_pay = amount_to_pay;
    }

    public String getUserName() {
        return user_name;
    }

    public Float getAmountToPay() {
        return amount_to_pay;
    }
}
