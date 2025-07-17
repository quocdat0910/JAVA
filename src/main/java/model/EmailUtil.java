package model;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {
 private static final String EMAIL_USERNAME = "nguyenquocdat091003@gmail.com"; // Thay bằng email của bạn
    private static final String EMAIL_PASSWORD = "ulyw novc xbtu kcbf"; // Thay bằng mật khẩu email của bạn

    public static boolean sendOTP(String recipientEmail, String otp) {
        String subject = "Verification OTP";
        String body = "Your OTP code is: " + otp;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Thay bằng host của email server của bạn (ví dụ: smtp.gmail.com cho Gmail)
        properties.put("mail.smtp.port", "587"); // Thay bằng port của email server của bạn (ví dụ: 587 cho Gmail)

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + recipientEmail);
            return true; // Trả về true nếu gửi thành công
        } catch (MessagingException e) {
            System.err.println("Error sending OTP to " + recipientEmail + ": " + e.getMessage());
            return false; // Trả về false nếu có lỗi khi gửi
        }
    }
}

