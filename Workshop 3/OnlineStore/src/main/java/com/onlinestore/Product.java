package com.onlinestore;

public class Product {

    private final String sku;
    private final String productName;
    private final double price;
    private final String department;

    public Product(String sku, String productName, double price, String department) {
        this.sku = sku;
        this.productName = productName;
        this.price = price;
        this.department = department;
    }

    public String getSku() {
        return sku;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-35s $%8.2f  %s", sku, productName, price, department);
    }
}
