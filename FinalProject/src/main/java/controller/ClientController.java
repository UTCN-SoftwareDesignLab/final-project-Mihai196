package controller;

import dto.CreditCardDTO;
import model.CreditCard;
import model.User;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.creditCard.CreditCardService;
import service.user.UserService;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/clientMenu", method = RequestMethod.GET)
    public String showUserOps(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        model.addAttribute("helloMessage","Hello " +auth.getName());
        model.addAttribute("options","What would you like to do today? ");
        return "clientMenu";
    }
    @RequestMapping(value = "/clientMenu", params = "addCreditCard",method = RequestMethod.POST)
    public String goToCreditCardMenu(Model model)
    {
        return "redirect:/creditCardMenu";
    }
    @RequestMapping(value ="/creditCardMenu" ,method = RequestMethod.GET)
    public String showCreditCardMenu(Model model)
    {
        model.addAttribute("creditCardDTO",new CreditCardDTO());
        return "/creditCardMenu";
    }
    @RequestMapping(value = "/backToClientMenu",method = RequestMethod.GET)
    public String backToMenu(Model model)
    {
        return "redirect:/clientMenu";
    }
    @RequestMapping(value = "/creditCardMenu",params = "addCreditCard",method = RequestMethod.POST)
    public String addCreditCard(Model model, @ModelAttribute("creditCardDTO") CreditCardDTO creditCardDTO)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername=userService.findByUsername(auth.getName());
        User loggedUser=usersByUsername.get(0);
        Notification<Boolean> creditCardNotification=creditCardService.addCreditCard(loggedUser.getId(),
                creditCardDTO.getBalance(),creditCardDTO.getBankName(),creditCardDTO.getCardNr());
        if(creditCardNotification.hasErrors())
        {
            model.addAttribute("notification",creditCardNotification.getFormattedErrors());
            return "creditCardMenu";
        }
        else
        {
            model.addAttribute("notification","A new credit card was added successfully");
            return "creditCardMenu";
        }
    }
    @RequestMapping(value = "/creditCardMenu",params = "viewCreditCard",method = RequestMethod.POST)
    public String viewCreditCards(Model model)
    {
        model.addAttribute("creditCardDTO",new CreditCardDTO());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername=userService.findByUsername(auth.getName());
        User loggedUser=usersByUsername.get(0);
        List<CreditCard> creditCards=creditCardService.findByClient(loggedUser.getId());
        model.addAttribute("creditCards",creditCards);
        return "creditCardMenu";


    }


}
