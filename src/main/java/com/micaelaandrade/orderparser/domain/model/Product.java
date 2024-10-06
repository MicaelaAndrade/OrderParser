package com.micaelaandrade.orderparser.domain.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private Long id;
    private Long productId;
    private BigDecimal value;
    private BigDecimal totalValue;

}