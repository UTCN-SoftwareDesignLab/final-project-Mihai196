package model.validation;

import model.ProductOrder;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderValidator {

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public ProductOrderValidator() {
        errors = new ArrayList<>();
    }

    public boolean validate(ProductOrder productOrder)
    {
        if(productOrder.getQuantity()>productOrder.getProduct().getStock())
        {
            errors.add("There are not enough products on the stock");
        }
        if(!productOrder.getClient().getRole().equals("client"))
        {
            errors.add("Orders can only be made by clients");
        }
        return errors.isEmpty();
    }
}
