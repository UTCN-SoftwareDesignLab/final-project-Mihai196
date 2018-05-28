package service;


import model.Product;
import model.builder.ProductBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.product.ProductRepository;
import service.product.ProductService;
import service.product.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest {

    ProductService productService;
    @Mock
    ProductRepository productRepository;

    @Before
    public void setup()
    {
        productService=new ProductServiceImpl(productRepository);
        List<Product> products=new ArrayList<>();
        Product product=new ProductBuilder().setName("Adidas X18").setSportCategory("Football").setTypeCategory("Shoes").setPrice(76.0)
                .setStock(150).setDescription("Great").build();
        products.add(product);
        Mockito.when(productRepository.findBySportCategory("Football")).thenReturn(products);
        Mockito.when(productRepository.findByTypeCategory("Shoes")).thenReturn(products);
    }

    @Test
    public void addProduct()
    {
        Assert.assertTrue(productService.addProduct("Adidas X18","Football","Shoes","Great",150,76.0).getResult());
    }

    @Test
    public void findAll()
    {
        Assert.assertTrue(productService.findAllProducts().size()==0);
    }

    @Test
    public void findBySport()
    {
        Assert.assertTrue(productService.findBySportCategory("Football").size()==1);
    }
    @Test
    public void findByType()
    {
        Assert.assertTrue(productService.findByTypeCategory("Shoes").size()==1);
    }
}
