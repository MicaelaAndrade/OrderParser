package com.micaelaandrade.orderparser;

import java.io.InputStream;


public interface ProcessOrderPort {
    void processOrder(InputStream file);
}
