package service.productOrder;

import model.Product;
import model.ProductOrder;
import model.validation.Notification;

import java.util.List;

public interface ProductOrderService {

    Notification<Boolean> addProductOrder(Long productId,Long clientId, int quantity);
    void deleteProductOrder(Long id);
    List<ProductOrder> findAllProductOrders();
    List<ProductOrder> findByClient(Long clientId);

}
