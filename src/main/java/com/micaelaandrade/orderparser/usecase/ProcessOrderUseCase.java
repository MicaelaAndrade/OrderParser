package com.micaelaandrade.orderparser.usecase;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.model.Order;
import com.micaelaandrade.orderparser.model.Product;
import com.micaelaandrade.orderparser.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ProcessOrderUseCase implements ProcessOrderPort {

    @Override
    public void processOrder(InputStream file) {
        Map<String, User> userMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String userId = line.substring(0, 10).trim();
                String userName = line.substring(10, 55).trim();
                String orderId = line.substring(55, 65).trim();
                String productId = line.substring(65, 75).trim();
                String value = line.substring(75, 87).trim();
                String date = line.substring(87, 95).trim();

                User user = userMap.computeIfAbsent(userId, id -> new User(id, userName));
                Order order = user.getOrders().stream()
                        .filter(o -> o.getOrderId().equals(orderId))
                        .findFirst()
                        .orElseGet(() -> {
                            Order newOrder = new Order(orderId, date);
                            user.getOrders().add(newOrder);
                            return newOrder;
                        });

                Product product = new Product(productId, value);
                order.getProducts().add(product);
            }
        } catch (IOException e) {
            log.error("[execute] Error reading file", e);
        }

        // Exibir informações no console
        userMap.values().forEach(user -> {
            System.out.println("User: " + user.getName());
            user.getOrders().forEach(order -> {
                System.out.println("  Order: " + order.getOrderId() + " Date: " + order.getDate());
                order.getProducts().forEach(product -> {
                    System.out.println("    Product: " + product.getProductId() + " Value: " + product.getValue());
                });
            });
        });
    }
}