package model.builder;

import model.CommandOrder;

import java.sql.Date;


public class CommandOrderBuilder {

    private CommandOrder commandOrder;

    public CommandOrderBuilder()
    {
        commandOrder=new CommandOrder();
    }
    public CommandOrderBuilder setId(Long id)
    {
        commandOrder.setId(id);
        return this;
    }
    public CommandOrderBuilder setExpectedArrivalDate(Date expectedArrivalDate)
    {
        commandOrder.setExpectedArrivalDate(expectedArrivalDate);
        return this;
    }
    public CommandOrderBuilder setDeliveryCompany(String company)
    {
        commandOrder.setDeliveryCompany(company);
        return this;
    }
    public CommandOrder build()
    {
        return commandOrder;
    }
}
