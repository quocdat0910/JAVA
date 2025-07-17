    package com.mycompany.fx;

    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.geometry.Insets;
    import javafx.scene.Scene;
import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.stage.Stage;
    import model.DatabaseUtil;
    import static model.DatabaseUtil.generateOTP;
    import model.EmailUtil;

    public class OTPVerification extends Application {

        private Stage stage; // Stage của cửa sổ OTPVerification
        private String username;
        private String password;
        private String email;
        private Label infoLabel; // Label để hiển thị thông báo lỗi

        public OTPVerification(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        @Override
        public void start(Stage primaryStage) {
            this.stage = primaryStage;

            BorderPane root = new BorderPane();
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #9E6F4E;");

            Label titleLabel = new Label("Xác Thực OTP");
            titleLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 36));
            titleLabel.setTextFill(Color.WHITE);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            Label otpLabel = new Label("Nhập OTP:");
            otpLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
            otpLabel.setTextFill(Color.WHITE);
            TextField otpField = new TextField();
            otpField.setFont(Font.font("Times New Roman", 24));

            gridPane.add(otpLabel, 0, 0);
            gridPane.add(otpField, 1, 0);

            HBox buttons = new HBox(10);
            buttons.setPadding(new Insets(20, 0, 0, 0));
            Button verifyOTPButton = new Button("Xác Thực OTP");
            verifyOTPButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
            verifyOTPButton.setStyle("-fx-background-color: #8b5a2b; -fx-text-fill: white;");
            Button backButton = new Button("Trở Lại");
            backButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
            backButton.setStyle("-fx-background-color: #8b5a2b; -fx-text-fill: white;");

            verifyOTPButton.setOnAction(e -> {
                String otp = otpField.getText();
                if (otp.isEmpty()) {
                    showError("Vui lòng nhập OTP.");
                    return;
                }

                boolean isOTPValid = DatabaseUtil.verifyOTP(email, otp);
                if (isOTPValid) {
                    // Đăng ký người dùng sau khi xác thực OTP thành công
                    signUpUser(username, password, email);
                     Stage stage = (Stage) verifyOTPButton.getScene().getWindow();
                     stage.close();
                      Login login = new Login(); // Tạo một instance mới của Login
                     
                } else {
                    showError("OTP không hợp lệ. Vui lòng kiểm tra lại.");
                }
            });
            
            

            backButton.setOnAction(e -> stage.close());

            buttons.getChildren().addAll(verifyOTPButton, backButton);

            infoLabel = new Label();
            infoLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
            infoLabel.setTextFill(Color.RED);
            root.setBottom(infoLabel);

            root.setTop(titleLabel);
            root.setCenter(gridPane);
            root.setBottom(buttons);

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Xác Thực OTP");
            primaryStage.show();
        }

        private void showError(String message) {
            Platform.runLater(() -> {
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText(message);
            });
        }
     
     private void sendOTPEmail(String email, String otp) {
        EmailUtil.sendOTP(email, otp);
        Platform.runLater(() -> {
            infoLabel.setText("Mã OTP đã được gửi đến email của bạn.");
        });
    }
   
        private void signUpUser(String username, String password, String email) {
            // Đăng ký người dùng và lưu vào cơ sở dữ liệu
            boolean isSuccess = DatabaseUtil.signUp(username, password, email);

            if (isSuccess) {
                Platform.runLater(() -> {
                    infoLabel.setTextFill(Color.GREEN);
                    infoLabel.setText("Đăng ký thành công!");
                });
            } else {
                Platform.runLater(() -> {
                    infoLabel.setTextFill(Color.RED);
                    infoLabel.setText("Đăng ký không thành công. Vui lòng thử lại sau.");
                });
            }
        }
    

        public static void main(String[] args) {
            launch(args);
        }
    }
