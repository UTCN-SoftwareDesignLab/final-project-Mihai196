package service.rating;

import model.Product;
import model.Rating;
import model.User;
import model.builder.RatingBuilder;
import model.validation.Notification;
import model.validation.RatingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.product.ProductRepository;
import repository.rating.RatingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Notification<Boolean> addRating(User client, String productName, int value, String description) {

        List<Product> products=productRepository.findByName(productName);
        Product product=products.get(0);
        Rating rating=new RatingBuilder().setClient(client).setProduct(product).setValue(value).setDescription(description).build();
        RatingValidator ratingValidator=new RatingValidator();
        boolean ratingValidation=ratingValidator.validate(rating);
        Notification<Boolean> ratingNotification=new Notification<>();
        if(!ratingValidation)
        {
            ratingValidator.getErrors().forEach(ratingNotification::addError);
            ratingNotification.setResult(Boolean.FALSE);
        }
        else
        {
            ratingRepository.save(rating);
            ratingNotification.setResult(Boolean.TRUE);
        }
        return ratingNotification;

    }

    @Override
    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> findByClient(User client) {
        return ratingRepository.findByClient(client);
    }
}
