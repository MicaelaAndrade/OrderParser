package com.micaelaandrade.orderparser.usecase;

import com.micaelaandrade.orderparser.database.UserImpl;
import com.micaelaandrade.orderparser.database.entity.OrderEntity;
import com.micaelaandrade.orderparser.database.entity.UserEntity;
import com.micaelaandrade.orderparser.database.mapper.UserEntityMapper;
import com.micaelaandrade.orderparser.domain.mapper.OrderMapper;

import com.micaelaandrade.orderparser.domain.model.Order;
import com.micaelaandrade.orderparser.domain.model.User;
import com.micaelaandrade.orderparser.database.OrderImpl;
import com.micaelaandrade.orderparser.database.mapper.OrderEntityMapper;
import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProcessOrderUseCaseTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderEntityMapper orderEntityMapper;

    @Mock
    private OrderImpl orderImpl;

    @Mock
    private UserImpl userImpl;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private ProcessOrderUseCase processOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldSaveOrderSuccessfully() {
        User mockUser = User.builder()
                .id(1L)
                .userId(1234567890L)
                .name("John Doe")
                .build();

        List<Order> mockOrders = List.of(Order.builder().user(mockUser).build());


        when(orderEntityMapper.domainToEntity(anyMap())).thenReturn(List.of());

        processOrderUseCase.saveOrder(mockOrders);

        verify(orderImpl, times(1)).saveOrder(anyList());
    }

    @Test
    public void shouldGetOrdersByUserSuccessfully() {
        Long userId = 1234567890L;

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(userId);

        List<OrderEntity> mockOrderEntities = List.of(new OrderEntity());

        OrderResponseDto mockOrderResponseDto = new OrderResponseDto(
                userId,
                "John Doe",
                List.of()
        );

        when(userImpl.findByUserId(userId)).thenReturn(mockUserEntity);
        when(orderImpl.getOrderByUserId(mockUserEntity)).thenReturn(mockOrderEntities);
        when(orderEntityMapper.domainToDto(mockUserEntity, mockOrderEntities)).thenReturn(mockOrderResponseDto);

        OrderResponseDto result = processOrderUseCase.getOrdersByUser(userId);

        verify(userImpl, times(1)).findByUserId(userId);
        verify(orderImpl, times(1)).getOrderByUserId(mockUserEntity);
        verify(orderEntityMapper, times(1)).domainToDto(mockUserEntity, mockOrderEntities);

        assertNotNull(result, "O resultado não deveria ser nulo");
    }

    @Test
    public void shouldGetAllOrdersSuccessfully() {

        UserEntity mockUserEntity = new UserEntity();
        List<UserEntity> mockUsers = List.of(mockUserEntity);

        List<OrderEntity> mockOrderEntities = List.of(new OrderEntity());
        OrderResponseDto mockOrderResponseDto = new OrderResponseDto(
                1234567890L,
                "John Doe",
                List.of()
        );

        when(userImpl.findAll()).thenReturn(mockUsers);
        when(orderImpl.getOrderByUserId(mockUserEntity)).thenReturn(mockOrderEntities);
        when(orderEntityMapper.domainToDto(mockUserEntity, mockOrderEntities)).thenReturn(mockOrderResponseDto);

        List<OrderResponseDto> result = processOrderUseCase.getAllOrders();

        verify(userImpl, times(1)).findAll();
        verify(orderImpl, times(1)).getOrderByUserId(mockUserEntity);
        verify(orderEntityMapper, times(1)).domainToDto(mockUserEntity, mockOrderEntities);

        assertEquals(1, result.size(), "The list size should be 1");
        assertEquals(mockOrderResponseDto, result.get(0));
    }

    @Test
    public void shouldThrowExceptionWhenFetchingOrdersByUser() {
        Long userId = 1234567890L;

        when(userImpl.findByUserId(userId)).thenThrow(new RuntimeException("Simulated Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            processOrderUseCase.getOrdersByUser(userId);
        });

        assertEquals("Failed to fetch orders for user: " + userId, exception.getMessage());

        verify(userImpl, times(1)).findByUserId(userId);
        verify(orderImpl, never()).getOrderByUserId(any());
        verify(orderEntityMapper, never()).domainToDto(any(), any());
    }

    @Test
    public void shouldThrowExceptionWhenFetchingAllOrders() {

        when(userImpl.findAll()).thenThrow(new RuntimeException("Simulated Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            processOrderUseCase.getAllOrders();
        });

        assertEquals("Failed to fetch all orders", exception.getMessage());

        verify(userImpl, times(1)).findAll();
        verify(orderImpl, never()).getOrderByUserId(any());
        verify(orderEntityMapper, never()).domainToDto(any(), any());
    }

}
