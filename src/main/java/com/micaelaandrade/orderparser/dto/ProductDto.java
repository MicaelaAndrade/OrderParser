package com.micaelaandrade.orderparser.dto;


public class ProductDto {
    private String productId;
    private String value;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}