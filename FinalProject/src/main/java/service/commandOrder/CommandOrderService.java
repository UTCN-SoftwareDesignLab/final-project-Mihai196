package service.commandOrder;

import model.CommandOrder;
import model.validation.Notification;

import java.sql.Date;


public interface CommandOrderService {
    Notification<CommandOrder> addCommandOrder(Date expectedArrivalDate, String deliveryCompany);
    CommandOrder findById(long commandId);
}
