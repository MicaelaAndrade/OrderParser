package com.micaelaandrade.orderparser.domain.mapper;

import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.domain.model.Product;
import com.micaelaandrade.orderparser.domain.model.User;
import com.micaelaandrade.orderparser.dto.OrderDto;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class OrderMapper {

    public Order dtoToDomain(OrderDto orderDto) {

        var products = Product.builder()
                .productId(orderDto.productId())
                .value(orderDto.value())
                .build();

        var users = User.builder()
                .name(orderDto.userName())
                .userId(orderDto.userId())
                .build();

        return Order.builder()
                .total(orderDto.value())
                .date(orderDto.date())
                .orderId(orderDto.orderId())
                .products(List.of(products))
                .user(users)
                .build();
    }
}
