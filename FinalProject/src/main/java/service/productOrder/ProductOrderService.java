package service.productOrder;

import model.CommandOrder;
import model.Product;
import model.ProductOrder;
import model.User;
import model.validation.Notification;

import java.util.List;

public interface ProductOrderService {

    Notification<Boolean> addProductOrder(Long productId,Long clientId,Long commandOrderId, int quantity);
    void deleteProductOrder(Long id);
    List<ProductOrder> findAllProductOrders();
    List<ProductOrder> findByClient(User client);
    List<ProductOrder> findByCommandOrder(CommandOrder commandOrder);

}
