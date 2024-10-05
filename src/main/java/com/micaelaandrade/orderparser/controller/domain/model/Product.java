package com.micaelaandrade.orderparser.controller.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Product {
    private Long id;
    private Long productId;
    private BigDecimal value;

    }

