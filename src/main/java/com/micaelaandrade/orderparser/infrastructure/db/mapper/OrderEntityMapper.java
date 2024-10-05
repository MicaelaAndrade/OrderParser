package com.micaelaandrade.orderparser.infrastructure.db.mapper;

import com.micaelaandrade.orderparser.infrastructure.db.entity.OrderEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.ProductEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import com.micaelaandrade.orderparser.controller.domain.model.Order;
import com.micaelaandrade.orderparser.controller.domain.model.Product;
import com.micaelaandrade.orderparser.controller.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderEntityMapper {
    public List<OrderEntity> domainToEntity(Map<Long, List<Order>> orders) {
        List<OrderEntity> orderEntities = new ArrayList<>();
        orders.forEach((userId, orderList) -> {

            List<ProductEntity> productEntities = orderList.stream()
                    .flatMap(order -> order.getProducts().stream())
                    .map(product -> ProductEntity.builder()
                            .externalId(product.getProductId())
                            .value(product.getValue())
                            .build())
                    .collect(Collectors.toList());
            System.out.println(productEntities);


            for (Order order : orderList) {
                UserEntity userEntity = UserEntity.builder()
                        .externalId(order.getUser().getUserId())
                        .name(order.getUser().getName())
                        .build();


                OrderEntity orderEntity = OrderEntity.builder()
                        .externalId(order.getOrderId())
                        .orderDate(order.getDate())
                        .user(userEntity)
                        .productEntities(productEntities)
                        .build();

                orderEntities.add(orderEntity);
            }
        });
        return orderEntities;
    }

    private BigDecimal getTotalValue(List<ProductEntity> productEntityList) {
        return productEntityList.stream()
                .map(ProductEntity::getValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}