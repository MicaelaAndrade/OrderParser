package com.micaelaandrade.orderparser.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.dto.OrderDto;
import com.micaelaandrade.orderparser.mapper.OrderMapper;
import com.micaelaandrade.orderparser.model.Order;
import com.micaelaandrade.orderparser.model.Product;
import com.micaelaandrade.orderparser.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessOrderUseCase implements ProcessOrderPort {

    private final OrderMapper orderMapper;

    @Override
    public void processOrder(InputStream file) {
        Map<String, User> userMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            var orderFormart =
                    formatOrder(br);
            var domin = orderFormart.stream().map(orderDto -> orderMapper.dtoToDomain(orderDto)).toList();
            System.out.println(domin.get(0).getOrderId() + "Testeeeeeeeeeee'");
            saveOrder(domin);

        } catch (IOException e) {
            log.error("[execute] Error reading file", e);
        }
    }

    private List<OrderDto> formatOrder(BufferedReader br) throws IOException {

        List<OrderDto> orderDtoList = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
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


        }

        return orderDtoList;
    }

    private boolean isValidLine(String line) {
        return line.length() >= 95;
    }

    private String formatDate(String date) {
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    private void saveOrder(List <Order> order) {
        var orderItem = getOrderItem(order);
        var orderUser = getOrderUser(order);
        orderItemByUser(orderItem, orderUser);

    }

    private Map<Long, List<Order>> getOrderItem(List<Order> order) {
        Map<Long, List<Order>> orderPerItem = order.stream()
                .collect(groupingBy(Order::getOrderId));

        return orderPerItem;

    }

    private Map<User, List<Order>> getOrderUser(List<Order> order) {
        Map<User, List<Order>> orderPerUser = order.stream()
                .collect(groupingBy(Order::getUser));

        return orderPerUser;
    }

    private void orderItemByUser (Map<Long, List<Order>> orderItem, Map<User, List<Order>> orderUser) {
        orderItem.forEach((key, value) -> {
            orderUser.forEach((keyUser, valueUser) -> {
              if(valueUser.contains(value)){
                  System.out.println(value + "valueeeeeeeeeeeeeeItem");
                  System.out.println(valueUser + "valueeeeeeeeeeeeeeUser");
              }
            });
        });

    }

}
