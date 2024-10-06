package com.micaelaandrade.orderparser.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

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
