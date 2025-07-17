package model;

import java.sql.*;
import java.util.*;
import java.util.regex.*;
import javafx.collections.*;
import javax.mail.*;
import javax.mail.internet.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/Mydtbb";
    private static final String USER = "root";
    private static final String PASSWORD = "Quocdat0910.";
      

    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidUsername(String username) {
        return username.length() >= 8;
    }

    public static boolean loginAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return false;
    }

    public static ObservableList<Product> getProducts() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String query = "SELECT * FROM products";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String image = resultSet.getString("image");

                Product product = new Product(id, name, price, image);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public static boolean addProduct(String productName, double price, String image) {
        String sql = "INSERT INTO products (name, price, image) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productName);
            statement.setDouble(2, price);
            statement.setString(3, image);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, price = ?, image = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getImagePath());
            statement.setInt(4, product.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Admin> getAdmins() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admins";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                Admin admin = new Admin();
                admin.setId(id);
                admin.setUsername(username);
                admin.setPassword(password);
                admin.setEmail(email);
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Phương thức đăng ký người dùng sử dụng bảng admins
   public static boolean signUp(String username, String password, String email) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean success = false;

    try {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String checkQuery = "SELECT * FROM admins WHERE email = ?";
        stmt = conn.prepareStatement(checkQuery);
        stmt.setString(1, email);
        rs = stmt.executeQuery();

        if (rs.next()) {
            // Email đã tồn tại trong cơ sở dữ liệu
            System.out.println("Email đã tồn tại trong cơ sở dữ liệu.");
            success = false;
        } else {
            // Thực hiện thêm bản ghi mới
            String insertQuery = "INSERT INTO admins (username, password, email) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Đăng ký thành công.");
                success = true;
            } else {
                System.out.println("Đăng ký không thành công.");
                success = false;
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi khi thực hiện câu lệnh SQL: " + e.getMessage());
        success = false;
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }

    return success;
}


    // Phương thức lưu trữ OTP
   // Phương thức lưu trữ OTP
public static boolean storeOTP(String email, String otp) {
    try (Connection conn = getConnection()) {
        String storeOtpQuery = "INSERT INTO otp (email, otp_code) VALUES (?, ?)";
        PreparedStatement storeOtpStmt = conn.prepareStatement(storeOtpQuery);
        storeOtpStmt.setString(1, email);
        storeOtpStmt.setString(2, otp);
        storeOtpStmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// Phương thức xác thực OTP
public static boolean verifyOTP(String email, String otp) {
    try (Connection conn = getConnection()) {
        String verifyOtpQuery = "SELECT COUNT(*) FROM otp WHERE email = ? AND otp_code = ?";
        PreparedStatement verifyOtpStmt = conn.prepareStatement(verifyOtpQuery);
        verifyOtpStmt.setString(1, email);
        verifyOtpStmt.setString(2, otp);
        ResultSet rs = verifyOtpStmt.executeQuery();
        rs.next();
        
        int count = rs.getInt(1);
        System.out.println("DEBUG: Number of matching OTP records: " + count);
        
        return count > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public static boolean updatePassword(String email, String newPassword) {
        if (!isValidPassword(newPassword)) {
            System.out.println("Mật khẩu mới không hợp lệ.");
            return false;
        }

        try (Connection conn = getConnection()) {
            String updatePasswordQuery = "UPDATE admins SET password = ? WHERE email = ?";
            PreparedStatement updatePasswordStmt = conn.prepareStatement(updatePasswordQuery);
            updatePasswordStmt.setString(1, newPassword);
            updatePasswordStmt.setString(2, email);
            int rowsAffected = updatePasswordStmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Phương thức tạo OTP ngẫu nhiên
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
