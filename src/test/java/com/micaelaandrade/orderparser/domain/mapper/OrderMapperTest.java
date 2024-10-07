package com.micaelaandrade.orderparser.domain.mapper;

import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    public void shouldConvertOrderDtoToDomain() {

        OrderDto orderDto = new OrderDto(1L, "User", 1L, 1L, BigDecimal.TEN, LocalDate.now());
        Order order = orderMapper.dtoToDomain(orderDto);

        assertThat(order.getOrderId()).isEqualTo(orderDto.orderId());
        assertThat(order.getTotal()).isEqualTo(orderDto.value());
        assertThat(order.getDate()).isEqualTo(orderDto.date());

        assertThat(order.getProducts()).isNotEmpty();
        assertThat(order.getProducts().get(0).getProductId()).isEqualTo(orderDto.productId());
        assertThat(order.getProducts().get(0).getValue()).isEqualTo(orderDto.value());

        assertThat(order.getUser().getName()).isEqualTo(orderDto.userName());
        assertThat(order.getUser().getUserId()).isEqualTo(orderDto.userId());
    }
}
