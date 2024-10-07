package com.micaelaandrade.orderparser.controller;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import com.micaelaandrade.orderparser.dto.OrderResponseDto.Order;
import com.micaelaandrade.orderparser.dto.OrderResponseDto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private ProcessOrderPort processOrderPort;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldProcessFileSuccessfully() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getOriginalFilename()).thenReturn("orders.txt");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("file content".getBytes()));

        orderController.processFile(mockFile);
        verify(processOrderPort, times(1)).processOrder(any());
    }

    @Test
    public void shouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderController.processFile(mockFile));
        assertThat(exception.getMessage()).isEqualTo("File is empty");
    }

    @Test
    public void shouldThrowExceptionWhenFileIsNotText() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("application/pdf");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderController.processFile(mockFile));
        assertThat(exception.getMessage()).isEqualTo("Invalid file type");
    }

    @Test
    public void shouldThrowExceptionWhenFileHasInvalidExtension() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getOriginalFilename()).thenReturn("orders.csv");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderController.processFile(mockFile));
        assertThat(exception.getMessage()).isEqualTo("Invalid file extension");
    }

    @Test
    public void shouldGetAllOrdersByUserId() {

        List<Product> products = List.of(new Product(1L, BigDecimal.TEN));
        List<Order> orders = List.of(new Order(1L, BigDecimal.TEN, LocalDate.now(), products, BigDecimal.TEN));

        OrderResponseDto mockResponse = new OrderResponseDto(1L, "Micaela", orders);

        when(processOrderPort.getOrdersByUser(1L)).thenReturn(mockResponse);

        OrderResponseDto response = orderController.getAllOrdersByUserId(1L);
        assertThat(response).isEqualTo(mockResponse);
        verify(processOrderPort, times(1)).getOrdersByUser(1L);
    }

    @Test
    public void shouldGetAllOrders() {

        List<Product> products = List.of(new Product(1L, BigDecimal.TEN));

        List<Order> orders = List.of(new Order(1L, BigDecimal.TEN, LocalDate.now(), products, BigDecimal.TEN));

        List<OrderResponseDto> mockResponse = List.of(new OrderResponseDto(1L, "Micaela", orders));

        when(processOrderPort.getAllOrders()).thenReturn(mockResponse);

        List<OrderResponseDto> response = orderController.getAllOrders();
        assertThat(response).isEqualTo(mockResponse);
        verify(processOrderPort, times(1)).getAllOrders();
    }
}
