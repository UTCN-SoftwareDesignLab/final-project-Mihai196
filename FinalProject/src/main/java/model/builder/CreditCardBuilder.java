package model.builder;

import model.CreditCard;
import model.User;

public class CreditCardBuilder {

    private CreditCard creditCard;

    public CreditCardBuilder()
    {
        creditCard=new CreditCard();
    }
    public CreditCardBuilder setId(Long id)
    {
        creditCard.setId(id);
        return this;
    }
    public CreditCardBuilder setClient(User client)
    {
        creditCard.setClient(client);
        return this;
    }
    public CreditCardBuilder setBalance(double balance)
    {
        creditCard.setBalance(balance);
        return this;
    }
    public CreditCardBuilder setBankName(String bankName)
    {
        creditCard.setBankName(bankName);
        return this;
    }
    public CreditCardBuilder setCardNr(Long cardNr)
    {
        creditCard.setCardNr(cardNr);
        return this;
    }
    public CreditCard build()
    {
        return creditCard;
    }
}
