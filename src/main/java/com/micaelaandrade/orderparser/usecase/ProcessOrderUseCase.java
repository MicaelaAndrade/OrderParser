package com.micaelaandrade.orderparser.usecase;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.database.UserImpl;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.domain.mapper.OrderMapper;
import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.dto.OrderDto;
import com.micaelaandrade.orderparser.database.OrderImpl;
import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.mapper.OrderEntityMapper;

import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessOrderUseCase implements ProcessOrderPort {

    private final OrderMapper orderMapper;
    private final OrderEntityMapper orderEntityMapper;
    private final OrderImpl orderImp;
    private final UserImpl userImp;

    @Override
    public void processOrder(InputStream file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            log.error("Import file");
            var orderFormat = formatOrder(br);
            var domain = orderFormat.stream()
                    .map(orderDto -> orderMapper.dtoToDomain(orderDto))
                    .toList();
            saveOrder(domain);
        } catch (IOException e) {
            log.error("Error reading file", e);
        } catch (Exception e) {
            log.error("Unexpected error processing the file", e);
        }
    }

    protected List<OrderDto> formatOrder(BufferedReader br) throws IOException {
        log.error("Formating file");
        List<OrderDto> orderDtoList = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            try {

                String userId = line.substring(0, 10).trim();
                String userName = line.substring(10, 55).trim();
                String orderId = line.substring(55, 65).trim();
                String productId = line.substring(65, 75).trim();
                String value = line.substring(75, 87).trim();
                String date = line.substring(87, 95).trim();

                orderDtoList.add(OrderDto.builder()
                        .userId(Long.valueOf(userId))
                        .userName(userName)
                        .orderId(Long.valueOf(orderId))
                        .productId(Long.valueOf(productId))
                        .value(new BigDecimal(value))
                        .date(LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE))
                        .build());
            } catch (NumberFormatException | DateTimeParseException e) {
                log.error("Error parsing line: {} - Exception: {}", line, e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error processing line: {} - Exception: {}", line, e.getMessage());
                throw e;
            }
        }

        return orderDtoList;
    }

     void saveOrder(List<Order> orders) {
        try {
            log.error("Saving Order");
            var orderItemByUser = orderByUser(orders);
            var order = orderEntityMapper.domainToEntity(orderItemByUser);
            orderImp.saveOrder(order);
        } catch (Exception e) {
            log.error("Error saving order", e);
            throw new RuntimeException("Failed to save orders", e);
        }
    }

    private Map<Long, List<Order>> orderByUser(List<Order> orderUser) {
        try {
            log.error("Finding all orders by user");
            Map<Long, List<Order>> userOrdersMap = new HashMap<>();
            orderUser.forEach(order -> {
                Long userId = order.getUser().getUserId();
                userOrdersMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(order);
            });
            return userOrdersMap;
        } catch (Exception e) {
            log.error("Error grouping orders by user", e);
            throw new RuntimeException("Failed to group orders by user", e);
        }
    }

    @Override
    public OrderResponseDto getOrdersByUser(Long userId) {
        try {
            var user = userImp.findByUserId(userId);
            List<OrderEntity> orders = orderImp.getOrderByUserId(user);
            return orderEntityMapper.domainToDto(user, orders);
        } catch (Exception e) {
            log.error("Error fetching orders for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch orders for user: " + userId, e);
        }
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        try {
            List<UserEntity> users = userImp.findAll();
            List<OrderResponseDto> orders = new ArrayList<>();
            users.forEach(user -> {
                List<OrderEntity> orderEntity = orderImp.getOrderByUserId(user);
                orders.add(orderEntityMapper.domainToDto(user, orderEntity));
            });
            return orders;
        } catch (Exception e) {
            log.error("Error fetching all orders", e);
            throw new RuntimeException("Failed to fetch all orders", e);
        }
    }
}