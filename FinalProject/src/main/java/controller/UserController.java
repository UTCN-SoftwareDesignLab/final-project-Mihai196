package controller;

import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController implements WebMvcConfigurer {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Model model,HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        if(session.getAttribute("errorMessage")!=null)
        {
            model.addAttribute("incorrect",(String)session.getAttribute("errorMessage"));
        }
        return "login";
    }
    @RequestMapping(value="/registration",method=RequestMethod.GET)
    public String reg()
    {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(Model model,@RequestParam("username") String username,@RequestParam("password")String password,@RequestParam("role") String role) {
        Notification<Boolean> userNotification=userService.addUser(username,password,role);
        if (userNotification.hasErrors())
        {
            model.addAttribute("error",userNotification.getFormattedErrors());
            return "registration";
        }
        else
        {
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/login",params = "createACC",method=RequestMethod.POST)
    public String regg(Model model)
    {
        return "redirect:/registration";

    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
