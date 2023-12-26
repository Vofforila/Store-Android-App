package com.example.firebase2;

public class Product {
    private final String itemName;
    private final String itemPrice;
    private final String itemURL;

    public Product(String itemName, String itemPrice, String itemURL) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemURL = itemURL;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemURL() {
        return itemURL;
    }
}
