package com.example.volunteersmilescare.Model;

import java.util.List;

public class Request {
    private String name;
    private String amount;
    private String orderId;
    private String Time;
    private String Phone;

    public Request() {
    }

    public Request(String name, String amount, String orderId, String time, String phone) {
        this.name = name;
        this.amount = amount;
        this.orderId = orderId;
        Time = time;
        Phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
