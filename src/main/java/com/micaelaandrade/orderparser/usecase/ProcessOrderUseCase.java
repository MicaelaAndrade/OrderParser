package com.micaelaandrade.orderparser.usecase;

import com.micaelaandrade.orderparser.ProcessOrderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Service
public class ProcessOrderUseCase implements ProcessOrderPort {

    @Override
    public void processOrder(InputStream file) {
        try (BufferedReader fileBuffered = new BufferedReader(new InputStreamReader(file))) {
            System.out.println(fileBuffered + " is being processed");
        } catch (IOException e) {
            log.error("[execute] Error reading file", e);
        }
    }
}
