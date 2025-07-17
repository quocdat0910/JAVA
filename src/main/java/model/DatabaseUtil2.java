package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseUtil2 {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Mydtbb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Quocdat0910.";

    public static ObservableList<Product> getProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image");

                products.add(new Product(id, name, price, imagePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return products;
    }

  public static void addToCart(int cartId, Product product, int quantity) {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        // Check if the product already exists in the cart
        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM cart WHERE pid = ?");
        checkStmt.setInt(1, product.getId());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            // If product exists, update the quantity and total
            int currentQty = rs.getInt("qty");
            double currentTotal = rs.getDouble("total");

            int newQty = currentQty + quantity;
            double newTotal = currentTotal + (product.getPrice() * quantity);

            String updateSql = "UPDATE cart SET qty = ?, total = ? WHERE pid = ?";
            stmt = conn.prepareStatement(updateSql);
            stmt.setInt(1, newQty);
            stmt.setDouble(2, newTotal);
            stmt.setInt(3, product.getId());
        } else {
            // If product does not exist, insert a new row
            String insertSql = "INSERT INTO cart (id, pid, name, qty, price, total) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setInt(1, cartId);
            stmt.setInt(2, product.getId());
            stmt.setString(3, product.getName());
            stmt.setInt(4, quantity);
            stmt.setDouble(5, product.getPrice());
            stmt.setDouble(6, product.getPrice() * quantity);
        }

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Product added to cart successfully.");
        } else {
            System.out.println("No rows affected.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



    public static void savePayment(String customerName, double total, LocalDate paymentDate, ObservableList<CartItem> cartItems) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Tạo một bản ghi mới trong bảng payment
            String insertSql = "INSERT INTO payment (cName, total, pdate, proid, pName) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customerName);
            stmt.setDouble(2, total);
            stmt.setDate(3, Date.valueOf(paymentDate));

            // Lặp qua các sản phẩm trong giỏ hàng để thêm vào cột proid và pName
            for (CartItem item : cartItems) {
                stmt.setInt(4, item.getProductId());
                stmt.setString(5, item.getName());
                stmt.executeUpdate();
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment saved successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<CartItem> getCartItems() {
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM cart");

            while (rs.next()) {
                int productId = rs.getInt("pid");
                String name = rs.getString("name");
                int quantity = rs.getInt("qty");
                double price = rs.getDouble("price");
                double total = rs.getDouble("total");

                CartItem item = new CartItem(productId, name, quantity, price, total);
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cartItems;
    }

    public static ObservableList<Product> getCartProducts() {
        ObservableList<Product> cartProducts = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM cart");

            while (rs.next()) {
                int id = rs.getInt("pid");
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                cartProducts.add(new Product(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cartProducts;
    }
    
    public static void clearCart() {
    Connection conn = null;
    Statement stmt = null;

    try {
        conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        stmt = conn.createStatement();

        // Xóa tất cả các mục trong bảng cart
        stmt.executeUpdate("DELETE FROM cart");

        System.out.println("Cart cleared successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
       public static int getNextPaymentId() {
        Connection conn = null;
        Statement stmt = null;
        int nextPaymentId = 1;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(pid) FROM payment");
            if (resultSet.next()) {
                nextPaymentId = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nextPaymentId;
    }
     public static ObservableList<Payment> getPayments() {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            String query = "SELECT * FROM payment";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int paymentId = rs.getInt("pid");
                String customerName = rs.getString("cName");
                int productId = rs.getInt("proid");
                String productName = rs.getString("pName");
                double totalPrice = rs.getDouble("total");
                LocalDate paymentDate = rs.getDate("pdate").toLocalDate();

                Payment payment = new Payment(paymentId, customerName, productId, productName, totalPrice, paymentDate);
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return payments;
    }
     public static Product getProductById(int productId) {
    Product product = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String query = "SELECT * FROM products WHERE id = ?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, productId);
        rs = stmt.executeQuery();

        if (rs.next()) {
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            String imagePath = rs.getString("image");
            product = new Product(productId, name, price, imagePath);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return product;
}

}
