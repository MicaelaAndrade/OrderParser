package com.micaelaandrade.orderparser.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record OrderDto (Long userId,
        String userName,
        Long orderId,
        Long productId,
        BigDecimal value,
        LocalDate date
){}