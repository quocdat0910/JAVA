package com.mycompany.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DatabaseUtil2;
import model.Product;
import model.CartItem;

import java.time.LocalDate;

public class Cart extends Application {

    private TableView<CartItem> table;
    private TextField paymentIdField;
    private TextField paymentDateField;
    private TextField taxField;
    private TextField subTotalField;
    private TextField totalField;
    private TextField customerNameField;
    private TextField cashField;
    private TextField changeField;
    private int cartId;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My Cart");

        cartId = DatabaseUtil2.getNextPaymentId();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: #F0F0F0;");

        paymentIdField = new TextField(String.valueOf(cartId));
        paymentIdField.setPromptText("Payment ID");
        paymentIdField.setEditable(false);

        paymentDateField = new TextField(LocalDate.now().toString());
        paymentDateField.setPromptText("Payment Date");
        paymentDateField.setEditable(false);

        taxField = new TextField("1.0");
        taxField.setPromptText("Tax ($)");
        taxField.setEditable(false);

        subTotalField = new TextField();
        subTotalField.setPromptText("Sub Total ($)");
        subTotalField.setEditable(false);

        totalField = new TextField();
        totalField.setPromptText("Total ($)");
        totalField.setEditable(false);

        customerNameField = new TextField();
        customerNameField.setPromptText("Customer Name");

        cashField = new TextField();
        cashField.setPromptText("Cash ($)");

        changeField = new TextField();
        changeField.setPromptText("Change ($)");
        changeField.setEditable(false);

        Button paymentButton = new Button("Payment");
        paymentButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        paymentButton.setOnAction(e -> handlePayment(primaryStage));

        Button backButton = new Button("Back To Order");
        backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        backButton.setOnAction(e -> {
            try {
                Order order = new Order();
                order.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CartItem, Integer> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<CartItem, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CartItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(productIdCol, productNameCol, quantityCol, priceCol);

        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(DatabaseUtil2.getCartItems());
        table.setItems(cartItems);

        gridPane.add(new Label("Payment ID:"), 0, 0);
        gridPane.add(paymentIdField, 1, 0);
        gridPane.add(new Label("Payment Date:"), 0, 1);
        gridPane.add(paymentDateField, 1, 1);
        gridPane.add(new Label("Tax:"), 0, 2);
        gridPane.add(taxField, 1, 2);
        gridPane.add(new Label("Sub Total:"), 0, 3);
        gridPane.add(subTotalField, 1, 3);
        gridPane.add(new Label("Total:"), 0, 4);
        gridPane.add(totalField, 1, 4);
        gridPane.add(new Label("Customer Name:"), 0, 5);
        gridPane.add(customerNameField, 1, 5);
        gridPane.add(new Label("Cash:"), 0, 6);
        gridPane.add(cashField, 1, 6);
        gridPane.add(new Label("Change:"), 0, 7);
        gridPane.add(changeField, 1, 7);
        gridPane.add(paymentButton, 1, 8);
        gridPane.add(backButton, 0, 8);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(table, gridPane);

        Scene scene = new Scene(vBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        calculateSubTotalAndTotal();
    }

    private void calculateSubTotalAndTotal() {
        double subTotal = table.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double tax = Double.parseDouble(taxField.getText());
        double total = subTotal + tax;

        subTotalField.setText(String.valueOf(subTotal));
        totalField.setText(String.valueOf(total));
    }

    private void handlePayment(Stage primaryStage) {
        String customerName = customerNameField.getText();
        double total = Double.parseDouble(totalField.getText());
        LocalDate paymentDate = LocalDate.now();

        double cash = Double.parseDouble(cashField.getText());
        double subTotal = Double.parseDouble(subTotalField.getText());

        if (cash >= subTotal) {
            double change = cash - subTotal;
            changeField.setText(String.valueOf(change));

            ObservableList<CartItem> cartItems = table.getItems();
            DatabaseUtil2.savePayment(customerName, total, paymentDate, cartItems);

            // Xóa các sản phẩm khỏi bảng `cart` trong cơ sở dữ liệu
            DatabaseUtil2.clearCart();

            // Xóa các sản phẩm khỏi `TableView`
            table.getItems().clear();

            cartId = DatabaseUtil2.getNextPaymentId();
            paymentIdField.setText(String.valueOf(cartId));
            paymentDateField.setText(LocalDate.now().toString());
            subTotalField.clear();
            totalField.clear();
            customerNameField.clear();
            cashField.clear();
            changeField.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payment successful! Change: $" + change);
            alert.showAndWait();

            // Chuyển về trang Order
            try {
                Order order = new Order();
                order.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cash must be greater than or equal to Sub Total.");
            alert.showAndWait();
        }
    }

    public void updateCart(ObservableList<CartItem> cartItems) {
        table.setItems(cartItems);
        calculateSubTotalAndTotal();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
