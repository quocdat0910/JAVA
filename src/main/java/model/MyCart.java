package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MyCart {
    private int cartId;
    private ObservableList<Product> products;

    public MyCart(int cartId) {
        this.cartId = cartId;
        this.products = FXCollections.observableArrayList();
    }
    

    public int getCartId() {
        return cartId;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void clearCart() {
        products.clear();
    }

    public double getTotal() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}

