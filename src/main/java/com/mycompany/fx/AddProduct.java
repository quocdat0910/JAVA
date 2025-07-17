package com.mycompany.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DatabaseUtil;

import java.io.File;

public class AddProduct extends Application {

    private Stage stage; // Stage của cửa sổ AddProduct

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Create the main layout BorderPane
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #49433c;");

        // Title label
        Label titleLabel = new Label("Add Product");
        titleLabel.setFont(Font.font("Tahoma", 36));
        titleLabel.setTextFill(Color.WHITE);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 20, 0));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // Product Name field
        TextField productNameField = createTextField("Product Name");

        // Price field
        TextField priceField = createTextField("Price ($)");

        // Image field
        TextField imageField = new TextField();
        imageField.setPromptText("Image");
        imageField.setFont(Font.font("Tahoma", 14));
        imageField.setPrefHeight(40);
        imageField.setEditable(false); // Disable editing

        // Browse button
        Button browseButton = new Button("Browse");
        browseButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        browseButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                imageField.setText(selectedFile.getPath());
            }
        });

        // Save button
        Button saveButton = new Button("Save");
        saveButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        saveButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            // Get data from fields
            String productName = productNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String image = imageField.getText();

            // Call method to add product to the database
            boolean added = DatabaseUtil.addProduct(productName, price, image);
            if (added) {
                System.out.println("Product added successfully.");
                clearFields(productNameField, priceField, imageField);
            } else {
                System.out.println("Failed to add product.");
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        backButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.close());

        // VBox for form fields and buttons
        VBox vbAddProduct = new VBox(10, productNameField, priceField, imageField, browseButton);
        vbAddProduct.setPrefHeight(250);
        vbAddProduct.setSpacing(15);

        // HBox for Save and Back buttons
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        buttons.getChildren().addAll(saveButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        // VBox for form and buttons
        VBox centerBox = new VBox(vbAddProduct, buttons);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        // Set up layout in BorderPane
        root.setTop(titleLabel);
        root.setCenter(centerBox);

        // Set up scene and stage
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Add Product");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(Font.font("Tahoma", 14));
        textField.setPrefHeight(40);
        return textField;
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}