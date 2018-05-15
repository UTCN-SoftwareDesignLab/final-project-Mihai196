package model.validation;

import model.CreditCard;

import java.util.ArrayList;
import java.util.List;

public class CreditCardValidator {

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public CreditCardValidator ()
    {
        errors=new ArrayList<>();
    }
    public boolean validate(CreditCard creditCard)
    {
        if(creditCard.getBalance()<0)
        {
            errors.add("Can't have a negative balance");
        }
        if(!creditCard.getBankName().equals("Raiffeisen")&&!(creditCard.getBankName().equals("BT"))&&!(creditCard.getBankName().equals("MasterCard")))
        {
            errors.add("Bank not available.We have contracts with Raiffeisen/BT/MasterCard");
        }
        if(String.valueOf(creditCard.getCardNr()).length()!=8)
        {
            errors.add("Credit card number must contain exactly 8 digits");
        }
        if(!(creditCard.getClient().getRole().equals("client")))
        {
            errors.add("Credit card are made for clients");
        }
        return errors.isEmpty();

    }
}
