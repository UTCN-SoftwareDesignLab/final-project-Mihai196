package service;

import model.CreditCard;
import model.User;
import model.builder.CreditCardBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.creditCard.CreditCardRepository;
import repository.user.UserRepository;
import service.creditCard.CreditCardService;
import service.creditCard.CreditCardServiceImpl;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
public class CreditCardServiceTest {

    CreditCardService creditCardService;
    @Mock
    CreditCardRepository creditCardRepository;
    @Mock
    UserRepository userRepository;

    @Before
    public void setup()
    {
        creditCardService=new CreditCardServiceImpl(creditCardRepository,userRepository);
        User user=new UserBuilder().setUsername("cornel@yahoo.com").setPassword("parola123!").setRole("administrator").build();
        List<CreditCard> creditCards=new ArrayList<>();
        long cardNr=12345678;
        CreditCard creditCard=new CreditCardBuilder().setCardNr(cardNr).setBankName("BT").setBalance(1000).setClient(user).build();
        creditCards.add(creditCard);
        long id=1;
        Mockito.when(creditCardService.findByClient(id)).thenReturn(creditCards);

    }

    @Test
    public void findAll()
    {
        Assert.assertTrue(creditCardService.findAll().size()==0);
    }
    @Test
    public void testtt()
    {
        User user=new UserBuilder().setUsername("cornel@yahoo.com").setPassword("parola123!").setRole("administrator").build();
        Assert.assertTrue(userRepository.save(user)==null);
    }
    @Test
    public void findByClient()
    {
        long id=1;
        Assert.assertTrue(creditCardService.findByClient(id).size()==0);
    }
}
