package com.micaelaandrade.orderparser.controller.domain.model.mapper;

import com.micaelaandrade.orderparser.dto.OrderDto;
import com.micaelaandrade.orderparser.controller.domain.model.Order;
import com.micaelaandrade.orderparser.controller.domain.model.Product;
import com.micaelaandrade.orderparser.controller.domain.model.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Component
public class OrderMapper {

   public Order dtoToDomain(OrderDto orderDto) {

       var products = Product.builder()
               .id(orderDto.productId())
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

