package com.mycompany.fx;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.DatabaseUtil;
import static model.DatabaseUtil.generateOTP;
import model.EmailUtil;

public class SignUp extends Application {

    private Stage stage; // Stage của cửa sổ SignUp
    private Label infoLabel; // Label để hiển thị thông báo lỗi

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:  #49433c;;");

        Label titleLabel = new Label("Sign Up");
        titleLabel.setFont(Font.font("Tahoma", 36));
        titleLabel.setTextFill(Color.WHITE);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 20, 0));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        
        TextField userNameField = new TextField();
        userNameField.setPromptText("UserName");
        userNameField.setPrefHeight(40);
        userNameField.setFont(Font.font("Tahoma", 14));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setFont(Font.font("Tahoma", 14));

        PasswordField rePasswordField = new PasswordField();
        rePasswordField.setPromptText("confirm-password");
        rePasswordField.setPrefHeight(40);
        rePasswordField.setFont(Font.font("Tahoma", 14));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(40);
        emailField.setFont(Font.font("Tahoma", 14));
        
        VBox vbSignUp= new VBox(10, userNameField, passwordField, rePasswordField, emailField);
        vbSignUp.setPrefHeight(250);
        vbSignUp.setSpacing(15);

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        signUpButton.setStyle("-fx-background-color:#332C25; -fx-text-fill: white;");

        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        backButton.setStyle("-fx-background-color:#332C25; -fx-text-fill: white;");


      signUpButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            String rePassword = rePasswordField.getText();
            String email = emailField.getText();

            if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() || email.isEmpty()) {
                showError("Vui lòng điền đầy đủ thông tin.");
                return;
            }

            if (!isValidUsername(username)) {
                showError("Tên người dùng phải có ít nhất 8 ký tự.");
                return;
            }

            if (!password.equals(rePassword)) {
                showError("Mật khẩu không khớp.");
                return;
            }

            if (!isValidEmail(email)) {
                showError("Định dạng email không hợp lệ.");
                return;
            }

            
            String otp = generateOTP();
            boolean otpSent = EmailUtil.sendOTP(email, otp);
            if (!otpSent) {
                showError("Không thể gửi OTP đến địa chỉ email.");
                return;
            }

            // Store OTP in database
            boolean otpStored = DatabaseUtil.storeOTP(email, otp);
            if (!otpStored) {
                showError("Lỗi khi lưu trữ OTP vào cơ sở dữ liệu.");
                return;
            }

            // Proceed to OTP verification step
            verifyOTP(username, password, email);
        });

        backButton.setOnAction(e -> stage.close());

        buttons.getChildren().addAll(signUpButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        infoLabel = new Label();
        infoLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        infoLabel.setTextFill(Color.RED);

        VBox centerBox = new VBox(vbSignUp, buttons, infoLabel);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(titleLabel);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText(message);
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            infoLabel.setTextFill(Color.GREEN);
            infoLabel.setText(message);
        });
    }

    private void openLoginWindow() {
        stage.close(); // Đóng cửa sổ đăng ký

        // Mở cửa sổ mới chứa trang Login
        Stage loginStage = new Stage();
        Login login = new Login();
        login.start(loginStage);
    }
     private void verifyOTP(String username, String password, String email) {
        stage.close();
        Stage otpStage = new Stage();
        OTPVerification otpVerification = new OTPVerification(username, password, email);
        otpVerification.start(otpStage);
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 8;
    }

    private boolean isValidEmail(String email) {
        
        return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }
      private String generateOTP() {
        return String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
