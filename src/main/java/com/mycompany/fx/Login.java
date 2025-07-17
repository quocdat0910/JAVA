package com.mycompany.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DatabaseUtil;

public class Login extends Application {

    private StackPane passwordStack;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        
        Label titleLabel = new Label("2H COFFEE");
        
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Tahoma'");
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);
        
        ImageView coffeeImage= new ImageView(new Image(getClass().getResourceAsStream("/images/coffee-bean.png")));
        coffeeImage.setFitHeight(40);
        coffeeImage.setFitWidth(40);

       
        HBox hboxTitle= new HBox();
        hboxTitle.getChildren().addAll( titleLabel, coffeeImage);
        hboxTitle.setMargin(coffeeImage, new Insets(3));
        
       
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(20)); 
        
        TextField userNameField = new TextField();
        userNameField.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px; ");
        userNameField.setPromptText("UserName");

        passwordStack = new StackPane();
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px;");
        passwordField.setPromptText("Password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px;");
        visiblePasswordField.setVisible(false); // Ẩn đi ban đầu

        passwordStack.getChildren().addAll(passwordField, visiblePasswordField);

        ImageView hidePassword = new ImageView(new Image(getClass().getResourceAsStream("/images/hide.png")));
        hidePassword.setFitWidth(24);
        hidePassword.setFitHeight(24);

        ImageView showPassword = new ImageView(new Image(getClass().getResourceAsStream("/images/visible.png")));
        showPassword.setFitWidth(24);
        showPassword.setFitHeight(24);
        showPassword.setVisible(false); // Ẩn đi ban đầu

        HBox passwordIcons = new HBox(5);
        passwordIcons.getChildren().addAll(hidePassword, showPassword);
        passwordIcons.setStyle("-fx-padding: 10px;");

        
        hidePassword.setOnMouseClicked(e -> {
            passwordField.setVisible(false);
            passwordField.setManaged(false);

            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            hidePassword.setVisible(false);
            showPassword.setVisible(true);
        });

        showPassword.setOnMouseClicked(e -> {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);

            hidePassword.setVisible(true);
            showPassword.setVisible(false);
        });

        Button loginButton = new Button("Login");
loginButton.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px; -fx-background-color:#332C25; -fx-text-fill: white;");
loginButton.setOnAction(e -> {
    String username = userNameField.getText();
    String password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
        showError("Please enter username and password.");
        return;
    }

    boolean loggedIn = DatabaseUtil.loginAdmin(username, password);
    if (loggedIn) {
        showInfo("Login successful.");
        openHomePage(primaryStage);
    } else {
        showError("Invalid username or password.");
    }
});

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px;-fx-text-fill: white;-fx-background-color:#332C25");
        cancelButton.setOnAction(e -> primaryStage.close());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(loginButton, cancelButton);

        formContainer.getChildren().addAll(
               userNameField,
                passwordStack,
                passwordIcons,
                buttonContainer
        );

        Label forgotPasswordLink = new Label("Forgot Password?");
        forgotPasswordLink.setStyle(" -fx-font-weight: bold; -fx-underline: true;-fx-text-fill: black; ");
        forgotPasswordLink.setOnMouseClicked(e -> {
            // Open Forgot Password window
            Stage forgotPasswordStage = new Stage();
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.start(forgotPasswordStage);
        });

        Label signUpLabel = new Label("Don't have an account?");
        Label signUpLink = new Label("Sign Up");
        signUpLink.setStyle(" -fx-font-weight: bold; -fx-underline: true;-fx-text-fill: black");
        signUpLink.setOnMouseClicked(e -> {
            // Open Sign Up window
            Stage signUpStage = new Stage();
            SignUp signUp = new SignUp();
            signUp.start(signUpStage);
        });

        VBox linksContainer = new VBox(10);
        linksContainer.getChildren().addAll(forgotPasswordLink, signUpLabel, signUpLink);
        linksContainer.setPadding(new Insets(10, 0, 0, 0));

        VBox content = new VBox(20);
        content.getChildren().addAll(hboxTitle, formContainer, linksContainer);
        content.setPadding(new Insets(10, 20, 20, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color:white");
        
    
        ImageView loginImage= new ImageView(new Image(getClass().getResourceAsStream("/images/coffee.png")));
        
        Label lbLogin= new Label("COFFEE SHOP MANAGEMENT");
        lbLogin.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-family: 'Arial';");
        
        VBox vboxLeft= new VBox(30);
        vboxLeft.getChildren().addAll(loginImage, lbLogin);
        vboxLeft.setStyle("-fx-background-color:#332C25; -fx-alignment: center; -fx-padding: 20px;");
        vboxLeft.setPrefWidth(500);
             
        HBox hboxContent = new HBox();
        hboxContent.getChildren().addAll(vboxLeft, content);
        vboxLeft.setAlignment(Pos.TOP_CENTER);
        
        root.setCenter(hboxContent);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String message) {
        // Implement your error handling logic here (e.g., show an alert dialog)
        System.err.println("Error: " + message);
    }

    private void showInfo(String message) {
        // Implement your information display logic here (e.g., show an alert dialog)
        System.out.println("Info: " + message);
    }

    private void openHomePage(Stage primaryStage) {
        primaryStage.close(); // Đóng cửa sổ đăng nhập

        // Mở cửa sổ mới chứa trang Home
        Stage homeStage = new Stage();
        Home home = new Home();
        home.start(homeStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
