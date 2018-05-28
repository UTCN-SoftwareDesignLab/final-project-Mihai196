package service;

import model.User;
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
        Mockito.when(userRepository.save(user)).thenReturn(user);

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
}
