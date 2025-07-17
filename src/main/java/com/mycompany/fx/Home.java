package com.mycompany.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.MyCart;

public class Home extends Application {
    private boolean isLoggedIn = true;
    private MyCart cart = new MyCart(1); // Initialize MyCart with a cart ID

    private Label lblTime;
    private Label lblPM;
    private Label lblDay;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");

    @Override
    public void start(Stage primaryStage) {
       // Create the main container, BorderPane
        BorderPane root = new BorderPane();

        // Load the background image and handle exception if it fails to load
        ImageView backgroundImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/cafe.png"));
            backgroundImage.setImage(image);
            backgroundImage.setFitWidth(1200);  // Adjusted width for right side
            backgroundImage.setFitHeight(720);
            backgroundImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        // Create GridPane for buttons and labels
        GridPane gridPane = createGridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);

        // Create labels and other components
        lblTime = createLabel(LocalDateTime.now().format(timeFormatter), 24, Color.WHITE);
        lblPM = createLabel("PM", 24, Color.WHITE); // You might update this dynamically if needed
        lblDay = createLabel(LocalDateTime.now().format(dateFormatter), 24, Color.WHITE);

        Label lblTitle = createLabel("2H COFFEE", 36, Color.WHITE);

        // Create a HBox for the top labels
        HBox topLabels = new HBox(10, lblTime, lblPM, lblDay);
        topLabels.setPadding(new Insets(30, 10, 0, 10));
        lblTitle.setPadding(new Insets(10));
        
        
       // Create a StackPane for the right side
        StackPane rightSide = new StackPane();
        rightSide.getChildren().addAll(backgroundImage, lblTitle, topLabels);
        StackPane.setAlignment(backgroundImage, Pos.CENTER);
        StackPane.setAlignment(lblTitle, Pos.TOP_CENTER);

        // Create a button for login/logout
        Button loginButton = new Button(isLoggedIn ? "Log Out" : "Login");
        loginButton.setOnAction(e -> handleButtonClick("Log Out", loginButton, primaryStage));
        styleButton(loginButton);
        loginButton.setMinWidth(150); 
        loginButton.setPrefWidth(200);
        loginButton.setMinHeight(40); 
        loginButton.setPrefHeight(40);

        
        // Create ImageView for the additional image
        ImageView additionalImageView = new ImageView();
        try {
            Image additionalImage = new Image(getClass().getResourceAsStream("/images/coffee.png"));
            additionalImageView.setImage(additionalImage);
            additionalImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading additional image: " + e.getMessage());
        }

        Region spacer = new Region();      
        VBox.setVgrow(spacer, Priority.ALWAYS); // Allow spacer to grow and push loginButton to the bottom
        VBox.setMargin(additionalImageView, new Insets(30,10,0,10));
        // Create a VBox for the left side
        VBox leftSide = new VBox(20,  additionalImageView, gridPane, spacer, loginButton );
        leftSide.setPadding(new Insets(20));
        leftSide.setStyle("-fx-background-color:#332C25");
        leftSide.setAlignment(Pos.CENTER);
        
        

        // Add components to BorderPane
        root.setLeft(leftSide);
        root.setRight(rightSide);
        root.setAlignment(lblTitle, Pos.TOP_CENTER);
        BorderPane.setMargin(lblTitle, new Insets(20, 0, 0, 0));
        BorderPane.setAlignment(lblTitle, Pos.TOP_CENTER);

        // Update time and date labels every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        lblTime.setText(LocalDateTime.now().format(timeFormatter));
        lblDay.setText(LocalDateTime.now().format(dateFormatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Setup scene and stage
        Scene scene = new Scene(root, 1200, 720);
        primaryStage.setTitle("2H COFFEE - Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(20);

        // Create buttons
        String[] buttonLabels = {"Add Product", "Manage Products", "All Products", "Order", "View Orders", "Statistics"};
        for (int i = 0; i < buttonLabels.length; i++) {
            Button button = new Button(buttonLabels[i]);
            styleButton(button);
            button.setMinWidth(150); 
            button.setPrefWidth(200);
            button.setMinHeight(40); 
            button.setPrefHeight(40);
            // Register event for buttons
            button.setOnAction(e -> handleButtonClick(button.getText(), null, null));
            gridPane.add(button, 0, i);
        }

        return gridPane;
    }
    private void handleButtonClick(String buttonText, Button loginButton, Stage currentStage) {
        // Handle button click events
        if (buttonText.equals("Add Product")) {
            AddProduct addProduct = new AddProduct();
            addProduct.start(new Stage());
        } else if (buttonText.equals("Manage Products")) {
            ManageProducts mng = new ManageProducts();
            mng.start(new Stage());
        } else if (buttonText.equals("All Products")) {
            AllProduct allProduct = new AllProduct();
            allProduct.start(new Stage());
        } else if (buttonText.equals("Order")) {
            Order order = new Order();
            order.start(new Stage());
        } else if (buttonText.equals("View Orders")) {
            ViewOrders orders = new ViewOrders();
            orders.start(new Stage());
        } else if (buttonText.equals("Statistics")) {
            Statistics st = new Statistics();
            st.start(new Stage());
        } else if (buttonText.equals("Log Out")) {
            // Handle logout logic
            isLoggedIn = false;
            if (loginButton != null) {
                loginButton.setText("Login");
            }
            showSuccessMessage("Logged out successfully.");
            // Clear the cart
            cart.clearCart();
            // Add logout logic here (e.g., clear session, cookies, etc.)
            if (currentStage != null) {
                Login login = new Login();
                login.start(new Stage());
                currentStage.close();
            }
        }
    }

    private Label createLabel(String text, int fontSize, Color color) {
        Label label = new Label(text);
        label.setFont(new Font("Times New Roman", fontSize));
        label.setTextFill(color);
        return label;
    }

    private void styleButton(Button button) {
        button.setFont(new Font("Times New Roman", 20));
         button.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1px; -fx-text-fill: white; -fx-font-size: 14px;");

    // Apply styles for hover state using setOnMouseEntered
    button.setOnMouseEntered(e -> {
        button.setStyle("-fx-background-color: #554D3E; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 0px; " +
                        "-fx-font-size: 14px;");
    });

    // Apply styles for normal state using setOnMouseExited
    button.setOnMouseExited(e -> {
        button.setStyle("-fx-background-color: transparent; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px;");
    });
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
