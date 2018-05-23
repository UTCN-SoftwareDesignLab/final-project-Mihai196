package service.creditCard;

import model.CreditCard;
import model.validation.Notification;

import java.util.List;

public interface CreditCardService {

    Notification<Boolean> addCreditCard(Long clientId,double balance,String bankName,Long cardNr);
    Notification<Boolean> updateBalance(Long id,double balance);
    void deleteCreditCard(Long id);
    List<CreditCard> findAll();
    List<CreditCard> findByClient(Long clientId);
    CreditCard findById(long id);

}
