package com.micaelaandrade.orderparser.infrastructure.db;

import com.micaelaandrade.orderparser.infrastructure.db.entity.OrderEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.ProductEntity;
import com.micaelaandrade.orderparser.infrastructure.db.entity.UserEntity;
import com.micaelaandrade.orderparser.infrastructure.db.repository.OrderRepository;
import com.micaelaandrade.orderparser.infrastructure.db.repository.ProductRepository;
import com.micaelaandrade.orderparser.infrastructure.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
                ProductEntity savedProduct = productRepository.save(productEntity);
                updatedProductEntities.add(savedProduct);
            }
            existingOrder.setProductEntities(updatedProductEntities);
            orderRepository.save(existingOrder);
        });
    }

}
