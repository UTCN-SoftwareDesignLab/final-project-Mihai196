package service.rating;

import model.Rating;
import model.User;
import model.validation.Notification;

import java.util.List;

public interface RatingService {
    Notification<Boolean> addRating(User client, String productName, int value, String description);
    List<Rating> findAllRatings();
    List<Rating> findByClient(User client);
}
