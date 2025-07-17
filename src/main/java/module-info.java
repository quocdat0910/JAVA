    module com.mycompany.fx {
        requires java.sql;
        requires javafx.controls;
        requires javafx.fxml;
        exports com.mycompany.fx;
        opens model to javafx.base;
        requires javafx.base;
        requires java.mail;
        

        // Nếu bạn cần xuất các package khác, hãy thêm dòng tương ứng ở đây
        // exports com.mycompany.fx;
    }
