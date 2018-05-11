package service.productOrder;

import model.Product;
import model.ProductOrder;
import model.validation.Notification;

import java.util.List;

public interface ProductOrderService {

    Notification<Boolean> addProductOrder(Product product, int quantity);
    void deleteProductOrder(Long id);
    List<ProductOrder> findAllProductOrders();

}
