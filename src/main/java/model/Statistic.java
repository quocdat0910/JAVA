package model;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;

public class Statistic {

    // Phương thức thống kê số lượng sản phẩm trong giỏ hàng
    public static int countProducts() {
        List<Product> products = DatabaseUtil.getProducts();
        return products.size();
    }

    // Phương thức thống kê doanh thu tổng cộng
    public static double totalRevenue() {
        List<Payment> payments = DatabaseUtil2.getPayments();
        double totalRevenue = 0;
        for (Payment payment : payments) {
            totalRevenue += payment.getTotalPrice(); // Adjusted to getTotalPrice() instead of getTotal()
        }
        return totalRevenue;
    }

    // Phương thức thống kê doanh thu trong ngày hôm nay
    public static double todayRevenue() {
        List<Payment> payments = DatabaseUtil2.getPayments();
        double todayRevenue = 0;
        for (Payment payment : payments) {
            // Assume payment.getPaymentDate() returns the date of the payment
            if (payment.getPaymentDate().equals(LocalDate.now())) {
                todayRevenue += payment.getTotalPrice(); // Adjusted to getTotalPrice() instead of getTotal()
            }
        }
        return todayRevenue;
    }

    // Phương thức thống kê số lượng quản trị viên
    public static int countAdmins() {
        List<Admin> admins = DatabaseUtil.getAdmins();
        return admins.size();
    }

    // Phương thức lấy danh sách các thanh toán
    public static ObservableList<Payment> getAllPayments() {
        return DatabaseUtil2.getPayments();
    }
}
