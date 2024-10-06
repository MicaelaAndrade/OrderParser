package com.micaelaandrade.orderparser;

import com.micaelaandrade.orderparser.dto.OrderResponseDto;

import java.io.InputStream;
import java.util.List;


public interface ProcessOrderPort {
    void processOrder(InputStream file);
    OrderResponseDto getOrdersByUser(Long userId);
}
