package model.builder;

import model.Product;

public class ProductBuilder {

    private Product product;

    public ProductBuilder()
    {
        product=new Product();
    }
    public ProductBuilder setId(Long id)
    {
        product.setId(id);
        return this;
    }
    public ProductBuilder setName(String name)
    {
        product.setName(name);
        return this;
    }
    public ProductBuilder setSportCategory(String sportCategory)
    {
        product.setSportCategory(sportCategory);
        return this;
    }
    public ProductBuilder setTypeCategory(String typeCategory)
    {
        product.setTypeCategory(typeCategory);
        return this;
    }
    public ProductBuilder setDescription(String description)
    {
        product.setDescription(description);
        return this;
    }
    public ProductBuilder setStock(int stock)
    {
        product.setStock(stock);
        return this;
    }
    public ProductBuilder setPrice(double price)
    {
        product.setPrice(price);
        return this;
    }
    public Product build()
    {
        return product;
    }

}
