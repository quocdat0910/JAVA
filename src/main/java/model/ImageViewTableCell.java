/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Dat
 */public class ImageViewTableCell extends TableCell<Product, String> {
    private final ImageView imageView = new ImageView();

    @Override
    protected void updateItem(String imagePath, boolean empty) {
        super.updateItem(imagePath, empty);
        if (empty || imagePath == null) {
            setGraphic(null);
        } else {
            try {
                Image image = new Image(imagePath);
                imageView.setImage(image);
                imageView.setFitWidth(500); // Width of the image
                imageView.setFitHeight(500); // Height of the image
                imageView.setPreserveRatio(true); // Maintain aspect ratio
                setGraphic(imageView);
            } catch (IllegalArgumentException | NullPointerException e) {
                System.err.println("Error loading image: " + e.getMessage());
                setGraphic(null);
            }
        }
    }
}

