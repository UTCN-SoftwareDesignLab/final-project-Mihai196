package service.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Notification<Boolean> addUser(String username, String password, String role) {
        System.out.println("Hello from user service");
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        User user = new UserBuilder().setUsername(username).setPassword(password).setRole(role).build();
        UserValidator userValidator=new UserValidator();
        boolean userValidation=userValidator.validate(user);
        Notification<Boolean> userNotification= new Notification<>();
        if(!userValidation)
        {
            userValidator.getErrors().forEach(userNotification::addError);
            userNotification.setResult(Boolean.FALSE);
        }
        else
        {
            user.setPassword(enc.encode(password));
            userRepository.save(user);
            userNotification.setResult(Boolean.TRUE);
        }
        return userNotification;

    }

    @Override
    public Notification<Boolean> updateUser(Long id, String username, String password, String role) {
        Optional<User> userOptional=userRepository.findById(id);
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        User user=new User();
        if (userOptional.isPresent())
        {
            user=userOptional.get();
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        UserValidator userValidator=new UserValidator();
        boolean userValidation=userValidator.validate(user);
        Notification<Boolean> userNotification= new Notification<>();
        if(!userValidation)
        {
            userValidator.getErrors().forEach(userNotification::addError);
            userNotification.setResult(Boolean.FALSE);
        }
        else
        {
            user.setPassword(enc.encode(password));
            userRepository.save(user);
            userNotification.setResult(Boolean.TRUE);
        }
        return userNotification;
    }

    @Override
    public List<User> findByUsername(String username) {
        List<User> users=userRepository.findByUsername(username);
        if (users.isEmpty())
        {
            return null;
        }
        else
        {
            return users;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users=userRepository.findAll();
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        User user=new UserBuilder().setId(id).build();
        userRepository.delete(user);

    }
}
