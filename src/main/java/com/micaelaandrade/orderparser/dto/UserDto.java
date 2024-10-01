package com.micaelaandrade.orderparser.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private String userId;
    private String name;
    private List<OrderDto> orders = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }
}