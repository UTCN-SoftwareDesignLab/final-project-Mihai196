package model.validation;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final int MIN_PASSWORD_LENGTH = 8;

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

     public UserValidator() {
        errors = new ArrayList<>();
    }
    public void validateRole(String role)
    {
        if (!role.equals("administrator")&&!role.equals("employee")&&!role.equals("client"))
        {
            errors.add("Invalid role type.Available roles: administrator/employee/client");
        }
    }
    public boolean validate(User u)
    {
        validateUsername(u.getUsername());
        validatePassword(u.getPassword());
        validateRole(u.getRole());
        return errors.isEmpty();
    }
    public boolean validateAuth(User user, String password)
    {
        if (!user.getPassword().equals(password))
        {
            errors.add("Invalid email or password");
        }
        return errors.isEmpty();
    }
    private void validateUsername(String username) {
        if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(username).matches()) {
            errors.add("Invalid email!");
        }
    }
    private void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password too short!");
        }
        if (!containsSpecialCharacter(password)) {
            errors.add("Password must contain at least one special character!");
        }
        if (!containsDigit(password)) {
            errors.add("Password must contain at least one number!");
        }
    }
    private boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        return m.find();
    }

    private boolean containsDigit(String s) {
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;
    }
}
