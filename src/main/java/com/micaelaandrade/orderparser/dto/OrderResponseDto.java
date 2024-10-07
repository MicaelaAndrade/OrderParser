package com.micaelaandrade.orderparser.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record OrderResponseDto(
        Long user_id,
        String name,
        List<Order> orders,
        BigDecimal totalValue
) {
    @Builder
    public record Order(
            Long order_id,
            BigDecimal total,
            LocalDate date,
            List<Product> productList
    ) {
    }
    @Builder
    public record Product(
            Long product_id,
            BigDecimal value
    ) {
    }
}