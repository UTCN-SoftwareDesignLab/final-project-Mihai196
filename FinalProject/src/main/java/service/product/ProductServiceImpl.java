package service.product;

import model.Product;
import model.builder.ProductBuilder;
import model.validation.Notification;
import model.validation.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.product.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Notification<Boolean> addProduct(String name, String sportCategory,String typeCategory, String description, int stock, double price) {
        Product product = new ProductBuilder().
                setName(name).
                setSportCategory(sportCategory).
                setTypeCategory(typeCategory).
                setDescription(description).
                setStock(stock).
                setPrice(price).build();
        ProductValidator productValidator = new ProductValidator();
        boolean productValidation = productValidator.validate(product);
        Notification<Boolean> productNotification = new Notification<>();
        if (!productValidation) {
            productValidator.getErrors().forEach(productNotification::addError);
            productNotification.setResult(Boolean.FALSE);
        } else {
            productRepository.save(product);
            productNotification.setResult(Boolean.TRUE);
        }
        return productNotification;
    }

    @Override
    public Notification<Boolean> updateProductDetails(Long id, String name, String sportCategory,String typeCategory, double price) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = new Product();
        if (productOptional.isPresent())
            product = productOptional.get();
        product.setName(name);
        product.setSportCategory(sportCategory);
        product.setTypeCategory(typeCategory);
        product.setPrice(price);
        ProductValidator productValidator = new ProductValidator();
        boolean productValidation = productValidator.validate(product);
        Notification<Boolean> productNotification = new Notification<>();
        if (!productValidation) {
            productValidator.getErrors().forEach(productNotification::addError);
            productNotification.setResult(Boolean.FALSE);
        } else {
            productRepository.save(product);
            productNotification.setResult(Boolean.TRUE);
        }
        return productNotification;
    }

    @Override
    public Notification<Boolean> updateProductStock(Long id, int stock) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = new Product();
        if (productOptional.isPresent())
            product = productOptional.get();
        product.setStock(stock);
        ProductValidator productValidator = new ProductValidator();
        boolean productValidation = productValidator.validate(product);
        Notification<Boolean> productNotification = new Notification<>();
        if (!productValidation) {
            productValidator.getErrors().forEach(productNotification::addError);
            productNotification.setResult(Boolean.FALSE);
        } else {
            productRepository.save(product);
            productNotification.setResult(Boolean.TRUE);
        }
        return productNotification;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product=new ProductBuilder().setId(id).build();
        productRepository.delete(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    @Override
    public List<Product> findBySportCategory(String sportCategory) {
        return productRepository.findBySportCategory(sportCategory);
    }

    @Override
    public List<Product> findByTypeCategory(String typeCategory) {
        return productRepository.findByTypeCategory(typeCategory);
    }


}
