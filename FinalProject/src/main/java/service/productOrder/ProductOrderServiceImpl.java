package service.productOrder;

import model.Product;
import model.ProductOrder;
import model.User;
import model.builder.ProductOrderBuilder;
import model.validation.Notification;
import model.validation.ProductOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.product.ProductRepository;
import repository.productOrder.ProductOrderRepository;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Notification<Boolean> addProductOrder(Long productId,Long clientId, int quantity) {
        Optional<Product> productOptional=productRepository.findById(productId);
        Optional<User> clientOptional=userRepository.findById(clientId);
        Product product=new Product();
        User client=new User();
        if(productOptional.isPresent()&&clientOptional.isPresent()) {
            product = productOptional.get();
            client=clientOptional.get();
        }

        ProductOrder productOrder=new ProductOrderBuilder().setProduct(product).setClient(client).setQuantity(quantity).build();
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
        ProductOrder productOrder=new ProductOrderBuilder().setId(id).build();
        productOrderRepository.delete(productOrder);

    }

    @Override
    public List<ProductOrder> findAllProductOrders() {

        return productOrderRepository.findAll();
    }

    @Override
    public List<ProductOrder> findByClient(Long clientId) {
        Optional<User> clientOptional=userRepository.findById(clientId);
        User client=new User();
        if(clientOptional.isPresent())
        {
            client=clientOptional.get();
        }
        return productOrderRepository.findByClient(client);
    }
}
