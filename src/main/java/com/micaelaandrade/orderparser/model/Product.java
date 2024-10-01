package com.micaelaandrade.orderparser.model;

public class Product {
    private String productId;
    private String value;

    public Product(String productId, String value) {
        this.productId = productId;
        this.value = value;
    }

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
