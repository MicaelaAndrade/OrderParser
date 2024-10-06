package com.micaelaandrade.orderparser.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private Long id;
    private String name;
    private Long userId;
    private List<Order> orders;
}