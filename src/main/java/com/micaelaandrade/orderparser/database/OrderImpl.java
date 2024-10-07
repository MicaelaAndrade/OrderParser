package com.micaelaandrade.orderparser.database;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.repository.OrderRepository;
import com.micaelaandrade.orderparser.database.repository.ProductRepository;
import com.micaelaandrade.orderparser.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderImpl {

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

            List<ProductEntity> updatedProductEntities = orderEntity.getProductEntities().stream()
                    .map(productEntity -> {
                        productEntity.setOrder(existingOrder);
                        return productRepository.save(productEntity);
                    })
                    .collect(Collectors.toList());

            existingOrder.getProductEntities().stream()
                    .filter(existingProduct -> !updatedProductEntities.contains(existingProduct))
                    .forEach(productRepository::delete);


            existingOrder.setProductEntities(updatedProductEntities);
            orderRepository.save(existingOrder);
        });
    }

    public List<OrderEntity> getOrderByUserId(UserEntity user) {
        return orderRepository.findAllByUserId(user.getId());
    }

}
