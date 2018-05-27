package service.product;

import model.Product;
import model.validation.Notification;

import java.util.List;

public interface ProductService {

    Notification<Boolean> addProduct(String name,String sportCategory,String typeCategory,String description,int stock,double price);
    Notification<Boolean> updateProductDetails(Long id,String name,String sportCategory,String typeCategory,double price);
    Notification<Boolean> updateProductStock(Long id,int stock);
    void deleteProduct(Long id);
    List<Product> findAllProducts();
    Product findById(Long id);
    List<Product> findBySportCategory(String sportCategory);
    List<Product> findByTypeCategory(String typeCategory);
}
