package com.mycompany.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Statistic;

public class Statistics extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Statistics");

        // Create root pane (BorderPane)
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:  #554D3E");

        // Top panel (Title)
        Label title = new Label("Statistics");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: Tahoma");
        BorderPane.setAlignment(title, javafx.geometry.Pos.CENTER);
        root.setTop(title);

        // Center panel (Statistics panels)
        GridPane statisticsGrid = new GridPane();
        statisticsGrid.setHgap(30);
        statisticsGrid.setVgap(30);
        statisticsGrid.setPadding(new Insets(30));

        // Fetching data from Statistic class
        double totalRevenue = Statistic.totalRevenue();
        int totalProducts = Statistic.countProducts();
        double todayRevenue = Statistic.todayRevenue();
        int totalAdmins = Statistic.countAdmins();

        // Total Revenue panel
        VBox totalRevenuePanel = createStatisticPanel(String.format("$%.2f", totalRevenue), "Total Revenue");
        statisticsGrid.add(totalRevenuePanel, 0, 0);

        // Total Products panel
        VBox totalProductsPanel = createStatisticPanel(Integer.toString(totalProducts), "Total Products");
        statisticsGrid.add(totalProductsPanel, 1, 0);

        // Today Revenue panel
        VBox todayRevenuePanel = createStatisticPanel(String.format("$%.2f", todayRevenue), "Today Revenue");
        statisticsGrid.add(todayRevenuePanel, 0, 1);

        // Total Admin panel
        VBox totalAdminPanel = createStatisticPanel(Integer.toString(totalAdmins), "Total Admin");
        statisticsGrid.add(totalAdminPanel, 1, 1);

        root.setCenter(statisticsGrid);

        // Create scene and set it to stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create each statistic panel
    private VBox createStatisticPanel(String value, String label) {
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: #332C25; -fx-padding: 10px;");
        panel.setSpacing(10);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-family: 'Times New Roman'; -fx-text-fill: white;");
        Label labelLabel = new Label(label);
        labelLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman'; -fx-text-fill: white;");

        panel.getChildren().addAll(valueLabel, labelLabel);
        return panel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
