package service;

import model.User;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.user.UserRepository;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    UserService userService;
    @Mock
    UserRepository userRepository;

    @Before
    public void setup()
    {
        userService=new UserServiceImpl(userRepository);
        List<User> users=new ArrayList<User>();
        User user=new UserBuilder().setUsername("cornel@yahoo.com").setPassword("parola123!").setRole("administrator").build();
        users.add(user);
        Mockito.when(userRepository.findByUsername("cornel@yahoo.com")).thenReturn(users);
    }

    @Test
    public void addUser()
    {
        Assert.assertTrue(userService.addUser("mirel@yahoo.com","parola123!","administrator").getResult());
    }
    @Test
    public void sizeUsers()
    {
        Assert.assertTrue(userService.findAll().size()==0);
    }
    @Test
    public void updateUser()
    {
        long id=1;
        Assert.assertTrue(userService.updateUser(id,"cornel@yahoo.com","parola123!","employee").getResult());
    }
    @Test
    public void findByUsername()
    {
        List<User> users=userService.findByUsername("cornel@yahoo.com");
        Assert.assertTrue(users.size()==1);
    }
}
