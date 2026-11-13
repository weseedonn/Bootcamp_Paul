package com.onlinestore;

import java.util.ArrayList;
import java.util.List;

/**
 * The customer's shopping cart.
 * Adding the same product twice increases its quantity instead of making a second line.
 */
public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getSku().equalsIgnoreCase(product.getSku())) {
                item.increaseQuantity();
                return;
            }
        }
        items.add(new CartItem(product));
    }

    /**
     * Removes the whole line item with the given SKU.
     *
     * @return true if something was removed, false if the SKU was not in the cart
     */
    public boolean removeProduct(String sku) {
        return items.removeIf(item -> item.getProduct().getSku().equalsIgnoreCase(sku));
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
