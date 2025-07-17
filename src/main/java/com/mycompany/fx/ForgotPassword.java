package com.mycompany.fx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.DatabaseUtil;
import model.EmailUtil;

public class ForgotPassword extends Application {

    private Stage stage; // Stage của cửa sổ ForgotPassword
    private String generatedOTP;
    private String userEmail;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2196F3;"); // Background color

        Label titleLabel = new Label("Forgot Password");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        TextField emailField = new TextField();

        gridPane.add(emailLabel, 0, 0);
        gridPane.add(emailField, 1, 0);

        Button sendOTPButton = new Button("Send OTP");
        sendOTPButton.setStyle("-fx-font-size: 24px;");
        sendOTPButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                showError("Please enter email.");
            } else {
                generatedOTP = DatabaseUtil.generateOTP();
                userEmail = email;
                boolean otpStored = DatabaseUtil.storeOTP(email, generatedOTP);
                if (otpStored) {
                    boolean emailSent = EmailUtil.sendOTP(email, generatedOTP);
                    if (emailSent) {
                        showInfo("OTP sent to your email.");
                        showOTPVerificationScreen(email);
                    } else {
                        showError("Failed to send OTP. Please check your email.");
                    }
                } else {
                    showError("Failed to store OTP.");
                }
            }
        });

        HBox buttons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 24px;");
        backButton.setOnAction(e -> stage.close());

        buttons.getChildren().addAll(sendOTPButton, backButton);

        gridPane.add(buttons, 1, 1);

        root.setTop(titleLabel);
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 450, 300);

        primaryStage.setTitle("Forgot Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showOTPVerificationScreen(String email) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2196F3;"); // Background color

        Label titleLabel = new Label("Enter OTP");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label otpLabel = new Label("OTP:");
        otpLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        TextField otpField = new TextField();

        gridPane.add(otpLabel, 0, 0);
        gridPane.add(otpField, 1, 0);

        HBox buttons = new HBox(10);
        Button verifyButton = new Button("Verify");
        verifyButton.setStyle("-fx-font-size: 24px;");
        verifyButton.setOnAction(e -> {
            String enteredOTP = otpField.getText();
            if (enteredOTP.isEmpty()) {
                showError("Please enter OTP.");
            } else if (DatabaseUtil.verifyOTP(email, enteredOTP)) {
                showNewPasswordScreen(email);
            } else {
                showError("Invalid OTP. Please try again.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 24px;");
        backButton.setOnAction(e -> start(stage));

        buttons.getChildren().addAll(verifyButton, backButton);

        gridPane.add(buttons, 1, 1);

        root.setTop(titleLabel);
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
    }

    private void showNewPasswordScreen(String email) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2196F3;"); // Background color

        Label titleLabel = new Label("New Password");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        PasswordField newPasswordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        PasswordField confirmPasswordField = new PasswordField();

        gridPane.add(newPasswordLabel, 0, 0);
        gridPane.add(newPasswordField, 1, 0);
        gridPane.add(confirmPasswordLabel, 0, 1);
        gridPane.add(confirmPasswordField, 1, 1);

        HBox buttons = new HBox(10);
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 24px;");
        saveButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showError("Please enter and confirm your new password.");
            } else if (!newPassword.equals(confirmPassword)) {
                showError("Passwords do not match. Please try again.");
            } else {
                boolean passwordUpdated = DatabaseUtil.updatePassword(email, newPassword);
                if (passwordUpdated) {
                    showInfo("Password updated successfully.");
                    stage.close();
                    openLoginPage();
                } else {
                    showError("Failed to update password.");
                }
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 24px;");
        backButton.setOnAction(e -> start(stage));

        buttons.getChildren().addAll(saveButton, backButton);

        gridPane.add(buttons, 1, 2);

        root.setTop(titleLabel);
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
    }

    private void showError(String message) {
        // Implement your error handling logic here (e.g., show an alert dialog)
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        // Implement your information display logic here (e.g., show an alert dialog)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openLoginPage() {
        Stage loginStage = new Stage();
        Login login = new Login();
        login.start(loginStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
