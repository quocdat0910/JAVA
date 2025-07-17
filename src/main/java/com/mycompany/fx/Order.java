package com.mycompany.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.DatabaseUtil2;
import model.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.FontWeight;

public class Order extends Application {

    private TableView<Product> tableView = new TableView<>();
    private TextField productIdField = new TextField();
    private TextField productNameField = new TextField();
    private TextField quantityField = new TextField();
    private Label totalLabel = new Label("Total ($): 0.0");
    private Button cartButton = new Button("Cart");
    private Button addToCartButton = new Button("Add to Cart");

    private List<Product> cartItems = new ArrayList<>();
    private int cartId;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(900, 600);

        HBox topPane = new HBox(10);
        topPane.setPadding(new Insets(10));
        topPane.setStyle("-fx-background-color: #554D3E;");
  
        productIdField.setPromptText("Product ID");
        productIdField.setFont(Font.font("Tahoma", 14));
        productIdField.setPrefHeight(40);
        productIdField.setEditable(false);

        productNameField.setPromptText("Product Name");
        productNameField.setFont(Font.font("Tahoma", 14));
        productNameField.setPrefHeight(40);
        productNameField.setEditable(false);

        quantityField.setPromptText("Quantity");
        quantityField.setFont(Font.font("Tahoma", 14));
        quantityField.setPrefHeight(40);

        cartButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        cartButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        cartButton.setOnAction(e -> cartAction(primaryStage));

        addToCartButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        addToCartButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> addToCartAction());
        
        topPane.getChildren().addAll(productIdField, productNameField, quantityField, addToCartButton, cartButton);

        // Create table columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(50);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(100);

        TableColumn<Product, String> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imageCol.setCellFactory(param -> new Order.ImageViewTableCell<>()); // Use ImageViewTableCell
        imageCol.setMinWidth(100);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().addAll(idCol, nameCol, priceCol, imageCol);

        ObservableList<Product> data = FXCollections.observableArrayList(DatabaseUtil2.getProducts());
        tableView.setItems(data);

        root.setCenter(tableView);

        HBox bottomPane = new HBox();
        bottomPane.setPadding(new Insets(10));
        bottomPane.setStyle("-fx-background-color: #554D3E;");
        bottomPane.getChildren().add(totalLabel);

        root.setTop(topPane);
        root.setCenter(tableView);
        root.setBottom(bottomPane);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                productNameField.setText(newValue.getName());
                productIdField.setText(String.valueOf(newValue.getId()));
            }
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Order Frame");
        primaryStage.show();
    }

    private void cartAction(Stage primaryStage) {
        Cart cart = new Cart();
        cart.start(primaryStage);
    }

 private void addToCartAction() {
    try {
        int productId = Integer.parseInt(productIdField.getText());
        String productName = productNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        // Fetch product details from the database
        Product product = DatabaseUtil2.getProductById(productId); // Implement getProductById in DatabaseUtil2

        if (product != null) {
            DatabaseUtil2.addToCart(cartId, product, quantity);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added to cart successfully.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
            alert.showAndWait();
        }
    } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input.");
        alert.showAndWait();
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred.");
        alert.showAndWait();
    }
}




    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

   private static class ImageViewTableCell<S> extends javafx.scene.control.TableCell<S, String> {
        private final ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String imagePath, boolean empty) {
            super.updateItem(imagePath, empty);
            if (empty || imagePath == null) {
                setGraphic(null);
            } else {
                try {
                    Image image = new Image(new FileInputStream(imagePath));
                    imageView.setImage(image);
                    imageView.setFitWidth(80); // Set image width
                    imageView.setPreserveRatio(true); // Preserve aspect ratio
                    setGraphic(imageView);
                } catch (FileNotFoundException e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    setGraphic(null);
                }
            }
        }
    }
}
