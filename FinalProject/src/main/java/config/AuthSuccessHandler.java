package config;

import model.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Get the role of logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        String targetUrl = "";
        if (role.contains(Constants.Roles.ADMINISTRATOR)) targetUrl = "/userOps";
        else if (role.contains(Constants.Roles.EMPLOYEE)) targetUrl = "/employee";
        else if (role.contains(Constants.Roles.CLIENT))
            targetUrl = "/clientMenu";
        return targetUrl;
    }

}
