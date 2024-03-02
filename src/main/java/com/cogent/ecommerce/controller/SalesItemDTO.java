package com.cogent.ecommerce.controller;

public class SalesItemDTO {

    private int productId;
    private int quantitySold;
    private double totalPrice;

    public int getProductId() {
        return productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
