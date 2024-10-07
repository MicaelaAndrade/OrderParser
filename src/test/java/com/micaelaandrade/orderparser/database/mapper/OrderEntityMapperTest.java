package com.micaelaandrade.orderparser.database.mapper;

import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.ProductEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrderEntityMapperTest {

    @InjectMocks
    private OrderEntityMapper orderEntityMapper;

    @Test
    public void shouldConvertDomainToDto() {

        UserEntity userEntity = UserEntity.builder()
                .externalId(1L)
                .name("Micaela")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .externalId(1L)
                .value(BigDecimal.TEN)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .externalId(100L)
                .orderDate(LocalDate.of(2024, 6, 10))
                .productEntities(List.of(productEntity))
                .total(BigDecimal.TEN)
                .build();

        OrderResponseDto orderResponseDto = orderEntityMapper.domainToDto(userEntity, List.of(orderEntity));


        assertThat(orderResponseDto.user_id()).isEqualTo(1L);
        assertThat(orderResponseDto.name()).isEqualTo("Micaela");
        assertThat(orderResponseDto.orders()).isNotEmpty();
        assertThat(orderResponseDto.orders().get(0).order_id()).isEqualTo(100L);
        assertThat(orderResponseDto.orders().get(0).total()).isEqualTo(BigDecimal.TEN);
        assertThat(orderResponseDto.orders().get(0).productList().get(0).product_id()).isEqualTo(1L);
        assertThat(orderResponseDto.orders().get(0).productList().get(0).value()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void shouldConvertDomainToEntity() {

        UserEntity userEntity = UserEntity.builder()
                .externalId(1L)
                .name("Micaela")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .externalId(1L)
                .value(BigDecimal.TEN)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .externalId(100L)
                .orderDate(LocalDate.of(2024, 6, 10))
                .user(userEntity)
                .productEntities(List.of(productEntity))
                .total(BigDecimal.TEN)
                .build();

        Order order = Order.builder()
                .orderId(100L)
                .date(LocalDate.of(2024, 6, 10))
                .products(List.of(com.micaelaandrade.orderparser.domain.model.Product.builder()
                        .productId(1L)
                        .value(BigDecimal.TEN)
                        .build()))
                .user(com.micaelaandrade.orderparser.domain.model.User.builder()
                        .userId(1L)
                        .name("Micaela")
                        .build())
                .total(BigDecimal.TEN)
                .build();


        List<OrderEntity> orderEntities = orderEntityMapper.domainToEntity(Map.of(1L, List.of(order)));

        assertThat(orderEntities).isNotEmpty();
        assertThat(orderEntities.get(0).getExternalId()).isEqualTo(100L);
        assertThat(orderEntities.get(0).getTotal()).isEqualTo(BigDecimal.TEN);
        assertThat(orderEntities.get(0).getProductEntities().get(0).getExternalId()).isEqualTo(1L);
        assertThat(orderEntities.get(0).getProductEntities().get(0).getValue()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void shouldHandleNullProductValue() {
        UserEntity userEntity = UserEntity.builder()
                .externalId(1L)
                .name("Micaela")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .externalId(1L)
                .value(null)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .externalId(100L)
                .orderDate(LocalDate.of(2024, 6, 10))
                .productEntities(List.of(productEntity))
                .total(BigDecimal.ZERO)
                .build();

        OrderResponseDto orderResponseDto = orderEntityMapper.domainToDto(userEntity, List.of(orderEntity));

        assertThat(orderResponseDto.user_id()).isEqualTo(1L);
        assertThat(orderResponseDto.name()).isEqualTo("Micaela");
        assertThat(orderResponseDto.orders()).isNotEmpty();
        assertThat(orderResponseDto.orders().get(0).order_id()).isEqualTo(100L);

        assertThat(orderResponseDto.orders().get(0).productList().get(0).value()).isEqualTo(BigDecimal.ZERO);
    }
}
