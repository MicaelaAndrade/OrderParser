package com.micaelaandrade.orderparser.infrastructure.db.mapper;

import com.micaelaandrade.orderparser.infrastructure.db.entity.OrderEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.ProductEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import com.micaelaandrade.orderparser.model.Order;
import com.micaelaandrade.orderparser.model.Product;
import com.micaelaandrade.orderparser.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderEntityMapper {
    public static Order entityToDomain(OrderEntity orderEntity) {
        var products = orderEntity.getProductEntities().stream()
                .map(productEntity -> Product.builder()
                        .productId(productEntity.getExternalId())
                        .value(productEntity.getValue())
                        .build())
                .collect(Collectors.toList());

        var user = User.builder()
                .name(orderEntity.getUser().getName())
                .userId(orderEntity.getUser().getId())
                .id(orderEntity.getUser().getExternalId())
                .build();

        return Order.builder()
                .orderId(orderEntity.getId())
                .date(orderEntity.getOrderDate())
                .id(orderEntity.getExternalId())
                .products(products)
                .user(user)
                .total(orderEntity.getTotalValue())
                .build();
    }

    public static OrderEntity domainToEntity(Order order, User user) {
        var userEntity = UserEntity.builder()
                .externalId(user.getUserId())
                .name(user.getName())
                .id(user.getUserId())
                .build();

        var orderEntity = OrderEntity.builder()
                .id(order.getId())
                .orderDate(order.getDate())
                .user(userEntity)
                .externalId(order.getOrderId())
                .totalValue(order.getTotal())
                .build();

        var products = order.getProducts().parallelStream()
                .map(product -> ProductEntity.builder()
                        .externalId(product.getProductId())
                        .value(product.getValue())
                        .build())
                .toList();

        orderEntity.setProductEntities(products);

        return orderEntity;
    }
}
