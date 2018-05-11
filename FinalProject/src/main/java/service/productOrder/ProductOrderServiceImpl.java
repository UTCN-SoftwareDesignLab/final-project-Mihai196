package service.productOrder;

import model.Product;
import model.ProductOrder;
import model.builder.ProductOrderBuilder;
import model.validation.Notification;
import model.validation.ProductOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.productOrder.ProductOrderRepository;

import java.util.List;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;


    @Override
    public Notification<Boolean> addProductOrder(Product product, int quantity) {
        ProductOrder productOrder=new ProductOrderBuilder().setProduct(product).setQuantity(quantity).build();
        ProductOrderValidator productOrderValidator=new ProductOrderValidator();
        boolean productOrderValidation=productOrderValidator.validate(productOrder);
        Notification<Boolean> productOrderNotification=new Notification<>();
        if(!productOrderValidation)
        {
            productOrderValidator.getErrors().forEach(productOrderNotification::addError);
            productOrderNotification.setResult(Boolean.FALSE);
        }
        else
        {
            productOrderRepository.save(productOrder);
            productOrderNotification.setResult(Boolean.TRUE);
        }
        return productOrderNotification;
    }

    @Override
    public void deleteProductOrder(Long id) {

    }

    @Override
    public List<ProductOrder> findAllProductOrders() {
        return null;
    }
}
