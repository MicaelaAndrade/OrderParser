package com.micaelaandrade.orderparser.database;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.repository.OrderRepository;
import com.micaelaandrade.orderparser.database.repository.ProductRepository;
import com.micaelaandrade.orderparser.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderImp {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void saveOrder(List<OrderEntity> orderEntityList) {
        orderEntityList.forEach(orderEntity -> {
            UserEntity user = userRepository.findByExternalId(orderEntity.getUser().getExternalId())
                    .orElseGet(() -> userRepository.save(orderEntity.getUser()));
            orderEntity.setUser(user);

            OrderEntity existingOrder = orderRepository.findByExternalId(orderEntity.getExternalId())
                    .orElseGet(() -> orderRepository.save(orderEntity));

            List<ProductEntity> updatedProductEntities = new ArrayList<>();
            for (ProductEntity productEntity : orderEntity.getProductEntities()) {
                productEntity.setOrder(existingOrder);
                ProductEntity savedProduct = productRepository.save(productEntity);
                updatedProductEntities.add(savedProduct);
            }

            List<ProductEntity> existingProducts = existingOrder.getProductEntities();
            for (ProductEntity existingProduct : existingProducts) {
                if (!updatedProductEntities.contains(existingProduct)) {
                    productRepository.delete(existingProduct);
                }
            }

            existingOrder.setProductEntities(updatedProductEntities);

            orderRepository.save(existingOrder);
        });
    }

}
