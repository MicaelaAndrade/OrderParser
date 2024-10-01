package com.micaelaandrade.orderparser.mapper;

import com.micaelaandrade.orderparser.dto.OrderDto;
import com.micaelaandrade.orderparser.dto.ProductDto;
import com.micaelaandrade.orderparser.dto.UserDto;
import com.micaelaandrade.orderparser.model.Order;
import com.micaelaandrade.orderparser.model.Product;
import com.micaelaandrade.orderparser.model.User;

import java.util.stream.Collectors;

public class OrderMapper {

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setOrders(user.getOrders().stream().map(OrderMapper::toOrderDto).collect(Collectors.toList()));
        return userDto;
    }

    public static OrderDto toOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setDate(order.getDate());
        orderDto.setProducts(order.getProducts().stream().map(OrderMapper::toProductDto).collect(Collectors.toList()));
        return orderDto;
    }

    public static ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setValue(product.getValue());
        return productDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User(
                userDto.getUserId(),
                userDto.getName()
        );
        user.setUserId(userDto.getUserId());
        user.setName(userDto.getName());
        user.setOrders(userDto.getOrders().stream().map(OrderMapper::toOrder).collect(Collectors.toList()));
        return user;
    }

    public static Order toOrder(OrderDto orderDto) {
        Order order = new Order(
                orderDto.getOrderId(),
                orderDto.getDate()
        );
        order.setOrderId(orderDto.getOrderId());
        order.setDate(orderDto.getDate());
        order.setProducts(orderDto.getProducts().stream().map(OrderMapper::toProduct).collect(Collectors.toList()));
        return order;
    }

    public static Product toProduct(ProductDto productDto) {
        Product product = new Product(
                productDto.getProductId(),
                productDto.getValue()
        );
        product.setProductId(productDto.getProductId());
        product.setValue(productDto.getValue());
        return product;
    }
}