package com.mycompany.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Payment;

public class ViewOrders extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("View Payments");

        // Create TableView
        TableView<Payment> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns
        TableColumn<Payment, Integer> paymentIdCol = new TableColumn<>("Payment ID");
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

        TableColumn<Payment, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Payment, Integer> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Payment, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Payment, Double> totalPriceCol = new TableColumn<>("Total Price");
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Payment, String> paymentDateCol = new TableColumn<>("Payment Date");
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Add columns to TableView
        tableView.getColumns().addAll(paymentIdCol, customerNameCol, productIdCol, productNameCol, totalPriceCol, paymentDateCol);

        // Create root pane (BorderPane)
        BorderPane root = new BorderPane();
        root.setCenter(tableView);

        // Create scene and set it to stage
        Scene scene = new Scene(root, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Populate TableView with data from database
        ObservableList<Payment> payments = Payment.getPayments();
        tableView.setItems(payments);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
