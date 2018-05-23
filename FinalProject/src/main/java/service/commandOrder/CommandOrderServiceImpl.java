package service.commandOrder;

import model.CommandOrder;
import model.builder.CommandOrderBuilder;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.commandOrder.CommandOrderRepository;

import java.sql.Date;
import java.util.Optional;


@Service
public class CommandOrderServiceImpl implements CommandOrderService {
    @Autowired
    private CommandOrderRepository commandOrderRepository;
    @Override
    public Notification<CommandOrder> addCommandOrder(Date expectedArrivalDate, String deliveryCompany) {
        CommandOrder commandOrder=
                new CommandOrderBuilder().setExpectedArrivalDate(expectedArrivalDate).setDeliveryCompany(deliveryCompany).build();
        CommandOrder insertedCommandOrder=commandOrderRepository.save(commandOrder);
        Notification<CommandOrder> commandOrderNotification=new Notification<>();
        commandOrderNotification.setResult(insertedCommandOrder);
        return commandOrderNotification;
    }

    @Override
    public CommandOrder findById(long commandId) {
        Optional<CommandOrder> commandOrderOptional=commandOrderRepository.findById(commandId);
        return commandOrderOptional.orElse(null);
    }
}
