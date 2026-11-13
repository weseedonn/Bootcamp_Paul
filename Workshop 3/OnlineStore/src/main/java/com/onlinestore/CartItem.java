package com.onlinestore;

public class CartItem {

    private final Product product;
    private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    /** Price of this product multiplied by the quantity. */
    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-35s $%8.2f  x%-3d $%9.2f",
                product.getSku(), product.getProductName(), product.getPrice(),
                quantity, getLineTotal());
    }
}
