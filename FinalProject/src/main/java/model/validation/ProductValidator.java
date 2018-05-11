package model.validation;


import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductValidator {

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public ProductValidator ()
    {
        errors=new ArrayList<>();
    }

    public boolean validate(Product product)
    {
        if(!product.getSportCategory().equals("Football")&&!product.getSportCategory().equals("Tennis")&&!product.getSportCategory().equals("Running"))
        {
            errors.add("Product sport category is not correct. Available: Football/Tennis/Running");
        }
        if(!product.getTypeCategory().equals("Shoes")&&!product.getTypeCategory().equals("Ball")&&!product.getTypeCategory().equals("Racquet")&&
        !product.getTypeCategory().equals("Others") )
        {
            errors.add("Product type category is not correct. Available: Shoes/Ball/Racquet/Others");
        }
        if(product.getPrice()<0)
        {
            errors.add("Product price must be a positive number");
        }
        if(product.getStock()<0)
        {
            errors.add("Product stock be a positive number");
        }
        return errors.isEmpty();
    }

}
