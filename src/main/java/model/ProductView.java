
package model;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 *
 * @author Dat
 */
public class ProductView {
    private Product product; // Assume you have a Product object

    // Đặt hình ảnh vào ImageView
    public void setProductImage(ImageView imageView) {
        String imagePath = product.getImagePath();
        Image image = new Image("file:" + imagePath); // Tạo Image từ đường dẫn
        imageView.setImage(image); // Đặt hình ảnh vào ImageView
    }
}
