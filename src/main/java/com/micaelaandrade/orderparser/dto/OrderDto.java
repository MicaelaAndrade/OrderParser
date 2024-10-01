package com.micaelaandrade.orderparser.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String orderId;
    private String date;
    private List<ProductDto> products = new ArrayList<>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}