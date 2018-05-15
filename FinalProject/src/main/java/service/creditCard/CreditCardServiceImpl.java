package service.creditCard;

import model.CreditCard;
import model.User;
import model.builder.CreditCardBuilder;
import model.validation.CreditCardValidator;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.creditCard.CreditCardRepository;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService{

    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Notification<Boolean> addCreditCard(Long clientId, double balance, String bankName, Long cardNr) {
        Optional<User> clientOptional=userRepository.findById(clientId);
        User client=new User();
        if(clientOptional.isPresent())
        {
            client=clientOptional.get();
        }
        CreditCard creditCard=new CreditCardBuilder().setClient(client).setBalance(balance).setBankName(bankName).setCardNr(cardNr).build();
        CreditCardValidator creditCardValidator=new CreditCardValidator();
        boolean creditCardValidation=creditCardValidator.validate(creditCard);
        Notification<Boolean> creditCardNotification=new Notification<>();
        if(!creditCardValidation)
        {
            creditCardValidator.getErrors().forEach(creditCardNotification::addError);
            creditCardNotification.setResult(Boolean.FALSE);
        }
        else
        {
            creditCardRepository.save(creditCard);
            creditCardNotification.setResult(Boolean.TRUE);
        }
        return creditCardNotification;
    }

    @Override
    public Notification<Boolean> updateBalance(Long id, double balance) {
        Optional<CreditCard> creditCardOptional=creditCardRepository.findById(id);
        CreditCard creditCard=new CreditCard();
        if(creditCardOptional.isPresent())
        {
            creditCard=creditCardOptional.get();
        }
        creditCard.setBalance(creditCard.getBalance()-balance);
        CreditCardValidator creditCardValidator=new CreditCardValidator();
        boolean creditCardValidation=creditCardValidator.validate(creditCard);
        Notification<Boolean> creditCardNotification=new Notification<>();
        if(!creditCardValidation)
        {
            creditCardValidator.getErrors().forEach(creditCardNotification::addError);
            creditCardNotification.setResult(Boolean.FALSE);
        }
        else
        {
            creditCardRepository.save(creditCard);
            creditCardNotification.setResult(Boolean.TRUE);
        }
        return creditCardNotification;

    }

    @Override
    public void deleteCreditCard(Long id) {
        CreditCard creditCard=new CreditCardBuilder().setId(id).build();
        creditCardRepository.delete(creditCard);
    }

    @Override
    public List<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    @Override
    public List<CreditCard> findByClient(Long clientId) {
        Optional<User> clientOptional=userRepository.findById(clientId);
        User client=new User();
        if(clientOptional.isPresent())
        {
            client=clientOptional.get();
        }
        return creditCardRepository.findByClient(client);
    }
}
