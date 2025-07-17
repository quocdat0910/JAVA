package model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private String customerName;
    private int productId;
    private String productName;
    private double totalPrice;
    private LocalDate paymentDate;

    public Payment(int paymentId, String customerName, int productId, String productName, double totalPrice, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Methods for database operations

    public static ObservableList<Payment> getPayments() {
        return DatabaseUtil2.getPayments();
    }

}
