package com.example.notestore.Storage.Cart;

import com.example.notestore.Storage.Products.Product;

public class CartItem {
    int id;
    Product product;
    int quantity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CartItem(Product product, int quantity, int id) {
        this.product = product;
        this.quantity = quantity;
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
