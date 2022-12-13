package com.example.notestore.Storage.Cart;

import com.example.notestore.Storage.Products.Product;

public class Cart {
    private final Product product;
    private final int quantity;

    public Cart(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
