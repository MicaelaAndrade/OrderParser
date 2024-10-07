package com.micaelaandrade.orderparser.database.mapper;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderEntityMapper {

    public List<OrderEntity> domainToEntity(Map<Long, List<Order>> orders) {
        List<OrderEntity> orderEntities = new ArrayList<>();

        orders.forEach((userId, orderList) -> {
            for (Order order : orderList) {

                UserEntity userEntity = UserEntity.builder()
                        .externalId(order.getUser().getUserId())
                        .name(order.getUser().getName())
                        .build();

                List<ProductEntity> productEntities = order.getProducts().stream()
                        .map(product -> ProductEntity.builder()
                                .externalId(product.getProductId())
                                .value(product.getValue())
                                .build())
                        .collect(Collectors.toList());

                BigDecimal totalOrderValue = productEntities.stream()
                        .map(ProductEntity::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                OrderEntity orderEntity = OrderEntity.builder()
                        .externalId(order.getOrderId())
                        .orderDate(order.getDate())
                        .user(userEntity)
                        .productEntities(productEntities)
                        .total(totalOrderValue)
                        .build();

                orderEntities.add(orderEntity);
            }
        });

        return orderEntities;
    }

    public OrderResponseDto domainToDto(UserEntity user, List<OrderEntity> orders) {
        List<OrderResponseDto.Order> orderDtos = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (OrderEntity order : orders) {
            List<OrderResponseDto.Product> productList = new ArrayList<>();

            for (ProductEntity product : order.getProductEntities()) {
                productList.add(OrderResponseDto.Product.builder()
                        .product_id(product.getExternalId())
                        .value(product.getValue() != null ? product.getValue() : BigDecimal.ZERO)
                        .build());
            }

            BigDecimal total = productList.stream()
                    .map(OrderResponseDto.Product::value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalValue = totalValue.add(total);

            OrderResponseDto.Order orderDto = OrderResponseDto.Order.builder()
                    .order_id(order.getExternalId())
                    .total(total)
                    .date(order.getOrderDate())
                    .productList(productList)
                    .totalValue(totalValue)
                    .build();

            orderDtos.add(orderDto);
        }

        return OrderResponseDto.builder()
                .user_id(user.getExternalId())
                .name(user.getName())
                .orders(orderDtos)
                .build();
    }
}