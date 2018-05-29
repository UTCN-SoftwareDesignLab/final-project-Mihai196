package service;

import model.Product;
import model.Rating;
import model.User;
import model.builder.ProductBuilder;
import model.builder.RatingBuilder;
import model.builder.UserBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.product.ProductRepository;
import repository.rating.RatingRepository;
import service.rating.RatingService;
import service.rating.RatingServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class RatingServiceTest {

    RatingService ratingService;
    @Mock
    RatingRepository ratingRepository;

    @Mock
    ProductRepository productRepository;

    @Before
    public void setup()
    {
        ratingService=new RatingServiceImpl(ratingRepository,productRepository);
        User user=new UserBuilder().setUsername("cornel@yahoo.com").setPassword("parola123!").setRole("administrator").build();
        List<Rating> ratings=new ArrayList<>();
        Product product=new ProductBuilder().setName("Adidas X18").setSportCategory("Football").setTypeCategory("Shoes").setPrice(76.0)
                .setStock(150).setDescription("Great").build();
        long cardNr=12345678;
        Rating rating=new RatingBuilder().setClient(user).setDescription("Great").setValue(3).setProduct(product).build();
        ratings.add(rating);
        Mockito.when(ratingRepository.findAll()).thenReturn(ratings);
    }

    @Test
    public void findaAll()
    {
        Assert.assertTrue(ratingService.findAllRatings().size()==1);
    }

}
