package com.mycompany.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DatabaseUtil;
import model.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static javafx.application.Application.launch;

public class AllProduct extends Application {

    private TableView<Product> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        // Create table columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(50);
        idCol.setMaxWidth(100);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(100);

        TableColumn<Product, String> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imageCol.setCellFactory(param -> new ImageViewTableCell<>()); // Use ImageViewTableCell
        imageCol.setMinWidth(100);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add columns to TableView
        tableView.getColumns().addAll(idCol, nameCol, priceCol, imageCol);

        // Fetch data from database
        ObservableList<Product> data = FXCollections.observableArrayList(DatabaseUtil.getProducts());
        tableView.setItems(data);

        // Add TableView to root pane
        root.setCenter(tableView);

        // Create scene and show stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("All Products");
        primaryStage.show();
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
