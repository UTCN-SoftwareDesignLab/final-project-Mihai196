package model.builder;

import model.Activity;
import model.User;

import java.util.Date;

public class ActivityBuilder {

    private Activity activity;

    public ActivityBuilder ()
    {
        activity=new Activity();
    }

    public ActivityBuilder setId(Long id)
    {
        activity.setId(id);
        return this;
    }
    public ActivityBuilder setUser(User user)
    {
        activity.setUser(user);
        return this;
    }
    public ActivityBuilder setDate(Date date)
    {
        activity.setActivityDate(date);
        return this;
    }
    public ActivityBuilder setType(String type)
    {
        activity.setType(type);
        return this;
    }
    public Activity build()
    {
        return activity;
    }
}
