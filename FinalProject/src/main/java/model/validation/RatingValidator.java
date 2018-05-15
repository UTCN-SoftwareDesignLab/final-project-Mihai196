package model.validation;

import model.Rating;

import java.util.ArrayList;
import java.util.List;

public class RatingValidator {

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public RatingValidator()
    {
        errors=new ArrayList<>();
    }
    public boolean validate(Rating rating)
    {
        if(rating.getValue()<1&&rating.getValue()>5)
        {
            errors.add("Ratings can have 1 to 5 stars");
        }
        if(!(rating.getClient().getRole().equals("client")))
        {
            errors.add("Ratings can only be performed by clients");
        }
        return errors.isEmpty();
    }
}
