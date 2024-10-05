package com.micaelaandrade.orderparser.controller.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class User {
    private Long id;
    private String name;
    private Long userId;
    private List<Order> orders = new ArrayList<>();


}