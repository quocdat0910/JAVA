package com.mycompany.fx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DatabaseUtil;
import model.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ManageProducts extends Application {

    private TableView<Product> tableView = new TableView<>();
    private TextField productNameField = new TextField();
    private TextField priceField = new TextField();
    private TextField imageField = new TextField();
    private Button deleteButton = new Button("Delete");
    private Button updateButton = new Button("Update");
    private Button browseButton = new Button("Browse");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(900, 600);

        // Top pane with product fields and buttons
        HBox topPane = new HBox(10);
        topPane.setPadding(new Insets(10));
        topPane.setStyle("-fx-background-color: #554D3E;");

        productNameField.setPromptText("Product Name");
        priceField.setPromptText("Price ($)");
        imageField.setPromptText("Image URL");
         imageField.setEditable(false);

        browseButton.setOnAction(e -> browseImage());
        
        updateButton.setOnAction(e -> updateProduct());
        deleteButton.setOnAction(e -> deleteProduct());

        topPane.getChildren().addAll(productNameField, priceField, imageField, browseButton, updateButton, deleteButton);

        // Table columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1)); // 10% of table width

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3)); // 30% of table width

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2)); // 20% of table width

        TableColumn<Product, String> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imageCol.setCellFactory(param -> new ImageViewTableCell<>()); // Use ImageViewTableCell
        imageCol.setPrefWidth(150); // Set a fixed width for the image column

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(idCol, nameCol, priceCol, imageCol);

        // Load data from database
        loadProducts();

        // Handle selection change in TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldProduct, newProduct) -> {
            if (newProduct != null) {
                productNameField.setText(newProduct.getName());
                priceField.setText(String.valueOf(newProduct.getPrice()));
                imageField.setText(newProduct.getImagePath());
            }
        });

           browseButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        browseButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        browseButton.setOnAction(e -> browseImage());

        updateButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        updateButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateProduct());

        deleteButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        deleteButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteProduct());

        // Add components to root pane
        root.setTop(topPane);
        root.setCenter(tableView);

        // Create scene and show stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Products");
        primaryStage.show();
    }

    private void loadProducts() {
        ObservableList<Product> products = DatabaseUtil.getProducts();
        tableView.setItems(products);
    }

    private void updateProduct() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            selectedProduct.setName(productNameField.getText());
            selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
            selectedProduct.setImagePath(imageField.getText());

            if (DatabaseUtil.updateProduct(selectedProduct)) {
                loadProducts();
            }
        }
    }

    private void deleteProduct() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (DatabaseUtil.deleteProduct(selectedProduct.getId())) {
                loadProducts();
                clearFields();
            }
        }
    }

    private void clearFields() {
        productNameField.clear();
        priceField.clear();
        imageField.clear();
    }

    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                imageField.setText(selectedFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Cell factory class for displaying ImageView in TableColumn
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
                    imageView.setFitWidth(50); // Set image width
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
