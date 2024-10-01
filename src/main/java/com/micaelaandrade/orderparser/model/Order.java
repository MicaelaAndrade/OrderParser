package com.micaelaandrade.orderparser.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String date;
    private List<Product> products = new ArrayList<>();

    public Order(String orderId, String date) {
        this.orderId = orderId;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
