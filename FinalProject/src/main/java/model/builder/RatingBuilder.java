package model.builder;

import model.Product;
import model.Rating;
import model.User;

public class RatingBuilder {

    private Rating rating;

    public RatingBuilder ()
    {
        rating=new Rating();
    }
    public RatingBuilder setId(Long id)
    {
        rating.setId(id);
        return this;
    }
    public RatingBuilder setClient(User client)
    {
        rating.setClient(client);
        return this;
    }
    public RatingBuilder setProduct(Product product)
    {
        rating.setProduct(product);
        return this;
    }
    public RatingBuilder setValue(int value)
    {
        rating.setValue(value);
        return this;
    }
    public RatingBuilder setDescription(String description)
    {
        rating.setDescription(description);
        return this;
    }
    public Rating build()
    {
        return rating;
    }
}
