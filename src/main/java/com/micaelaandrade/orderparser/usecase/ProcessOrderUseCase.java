package com.micaelaandrade.orderparser.usecase;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.dto.OrderDto;
import com.micaelaandrade.orderparser.infrastructure.db.mapper.OrderEntityMapper;
import com.micaelaandrade.orderparser.controller.domain.model.mapper.OrderMapper;
import com.micaelaandrade.orderparser.controller.domain.model.Order;
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
    private final OrderImp orderImp;

    @Override
    public void processOrder(InputStream file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            log.error("Import file");
            var orderFormat = formatOrder(br);
            var domain = orderFormat.stream().map(orderDto -> orderMapper.dtoToDomain(orderDto)).toList();
            saveOrder(domain);
        } catch (IOException e) {
            log.error("Error reading file", e);
        }
    }

    private List<OrderDto> formatOrder(BufferedReader br) throws IOException {
        log.error("Formating file");

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

    private void saveOrder(List<Order> orders) {
        log.error("Save Order");
        var orderItemByUser = orderByUser(orders);
        var order = orderEntityMapper.domainToEntity(orderItemByUser);
        orderImp.saveOrder(order);
    }

    private Map<Long, List<Order>> orderByUser(List<Order> orderUser) {
        log.error("Find all orders by user");
        Map<Long, List<Order>> userOrdersMap = new HashMap<>();
        orderUser.stream()
                .forEach(order -> {
                    Long userId = order.getUser().getUserId();
                    userOrdersMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(order);
                });
        return userOrdersMap;
    }
}