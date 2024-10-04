package com.micaelaandrade.orderparser.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Order {
    private Long id;
    private BigDecimal total;
    private LocalDate date;
    private Long orderId;
    private List<Product> products;
    private User user;



}
