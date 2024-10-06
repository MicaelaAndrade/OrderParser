package com.micaelaandrade.orderparser.controller;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import com.micaelaandrade.orderparser.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final ProcessOrderPort processOrderPort;

    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void processFile(@RequestParam("file") MultipartFile file) throws Exception {
        validateFile(file);
        processOrderPort.processOrder(file.getInputStream());

    }

    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!file.getContentType().equals("text/plain")) {
            throw new IllegalArgumentException("Invalid file type");
        }
        if (!file.getOriginalFilename().contains(".txt")) {
            throw new IllegalArgumentException("Invalid file extension");
        }
    }

    @GetMapping("/user/{userId}")
    public OrderResponseDto getAllOrdersByUserId(@PathVariable Long userId)  {
        return  processOrderPort.getOrdersByUser(userId);

    }

}
