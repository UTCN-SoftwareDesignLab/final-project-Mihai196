package config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage="";
        if(exception.getClass().isAssignableFrom(BadCredentialsException.class)){
            errorMessage="Invalid username or password.";
        }else{
            errorMessage=exception.getMessage();
        }
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login");
    }

}
