package com.micaelaandrade.orderparser.database;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.repository.OrderRepository;
import com.micaelaandrade.orderparser.database.repository.ProductRepository;
import com.micaelaandrade.orderparser.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderImpl orderImpl;

    private final LocalDate LOCAL_DATE = LocalDate.of(2024, 6, 10);

    @Test
    public void shouldSaveOrderWhenNewUserAndOrder() {
        OrderEntity orderEntity = buildOrderEntity();
        List<OrderEntity> orderEntityList = List.of(orderEntity);

        when(userRepository.findByExternalId(anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(orderEntity.getUser());
        when(orderRepository.findByExternalId(anyLong())).thenReturn(Optional.empty());
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(buildProductEntity());

        orderImpl.saveOrder(orderEntityList);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(orderRepository, times(2)).save(any(OrderEntity.class));
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    public void shouldSaveOrderWhenUserExists() {
        OrderEntity orderEntity = buildOrderEntity();
        List<OrderEntity> orderEntityList = List.of(orderEntity);

        when(userRepository.findByExternalId(anyLong())).thenReturn(Optional.of(orderEntity.getUser()));
        when(orderRepository.findByExternalId(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(buildProductEntity());

        orderImpl.saveOrder(orderEntityList);

        verify(userRepository, never()).save(any(UserEntity.class));
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    public void shouldDeleteUnusedProducts() {
        OrderEntity orderEntity = buildOrderEntity();
        List<OrderEntity> orderEntityList = List.of(orderEntity);
        ProductEntity oldProduct = ProductEntity.builder().id(2L).value(BigDecimal.TEN).build();
        orderEntity.getProductEntities().add(oldProduct);

        when(userRepository.findByExternalId(anyLong())).thenReturn(Optional.of(orderEntity.getUser()));
        when(orderRepository.findByExternalId(anyLong())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(buildProductEntity());
        doNothing().when(productRepository).delete(any(ProductEntity.class));

        orderImpl.saveOrder(orderEntityList);

        verify(productRepository, times(1)).delete(any(ProductEntity.class));
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    public void shouldReturnOrdersForUser() {
        UserEntity userEntity = buildUserEntity();

        List<OrderEntity> orderEntities = List.of(buildOrderEntity());

        when(orderRepository.findAllByUserId(userEntity.getId())).thenReturn(orderEntities);

        List<OrderEntity> response = orderImpl.getOrderByUserId(userEntity);

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getId()).isEqualTo(1L);
        verify(orderRepository, times(1)).findAllByUserId(userEntity.getId());
    }


    private OrderEntity buildOrderEntity() {
        var products = new ArrayList<ProductEntity>();
        products.add(buildProductEntity());

        return OrderEntity.builder()
                .total(BigDecimal.valueOf(10))
                .orderDate(LOCAL_DATE)
                .user(buildUserEntity())
                .productEntities(products)
                .id(1L)
                .externalId(1L)
                .build();
    }

    private ProductEntity buildProductEntity() {
        return ProductEntity.builder()
                .value(BigDecimal.valueOf(10))
                .id(1L)
                .externalId(1L)
                .build();
    }

    private UserEntity buildUserEntity() {
        return UserEntity.builder()
                .name("Micaela")
                .externalId(1L)
                .id(1L)
                .build();
    }
}
