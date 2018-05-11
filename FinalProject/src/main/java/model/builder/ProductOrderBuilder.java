package model.builder;

import model.Product;
import model.ProductOrder;

public class ProductOrderBuilder {

    private ProductOrder productOrder;

    public ProductOrderBuilder ()
    {
        productOrder=new ProductOrder();
    }
    public ProductOrderBuilder setId(Long id)
    {
        productOrder.setId(id);
        return this;
    }
    public ProductOrderBuilder setProduct(Product product)
    {
        productOrder.setProduct(product);
        return this;
    }
    public ProductOrderBuilder setQuantity(int quantity)
    {
        productOrder.setQuantity(quantity);
        return this;
    }
    public ProductOrder build()
    {
        return productOrder;
    }
}
