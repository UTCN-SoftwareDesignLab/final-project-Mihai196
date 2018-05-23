package controller;

import dto.CreditCardDTO;
import dto.ProductOrderDTO;
import model.*;
import model.builder.ProductOrderBuilder;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.commandOrder.CommandOrderService;
import service.creditCard.CreditCardService;
import service.product.ProductService;
import service.productOrder.ProductOrderService;
import service.rating.RatingService;
import service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommandOrderService commandOrderService;

    @RequestMapping(value = "/clientMenu", method = RequestMethod.GET)
    public String showUserOps(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (productOrders != null) {
            model.addAttribute("notification", "You have not finalized your command." +
                    "Make sure you finalize it before you log out.");
        }
        model.addAttribute("helloMessage", "Hello " + auth.getName());
        model.addAttribute("options", "What would you like to do today? ");
        return "clientMenu";
    }

    @RequestMapping(value = "/clientMenu", params = "addCreditCard", method = RequestMethod.POST)
    public String goToCreditCardMenu(Model model) {
        return "redirect:/creditCardMenu";
    }

    @RequestMapping(value = "/clientMenu", params = "buyProducts", method = RequestMethod.POST)
    public String goToBuyMenu(Model model) {
        return "redirect:/productOrderMenu";
    }

    @RequestMapping(value = "/clientMenu", params = "giveRating", method = RequestMethod.POST)
    public String goToRatingMenu(Model model) {
        return "redirect:/ratingMenu";
    }

    @RequestMapping(value = "/clientMenu", params = "trackOrder", method = RequestMethod.POST)
    public String goToTrackOrder(Model model) {
        return "redirect:/trackOrder";
    }

    @RequestMapping(value = "/creditCardMenu", method = RequestMethod.GET)
    public String showCreditCardMenu(Model model) {
        model.addAttribute("creditCardDTO", new CreditCardDTO());
        return "/creditCardMenu";
    }
    @RequestMapping(value="/trackOrder",method = RequestMethod.GET)
    public String showTrackOrder(Model model)
    {
        return "/trackOrder";
    }

    @RequestMapping(value = "/ratingMenu", method = RequestMethod.GET)
    public String showRatingMenu(Model model) {
        return "/ratingMenu";
    }

    @RequestMapping(value = "/productOrderMenu", method = RequestMethod.GET)
    public String showProductMenu(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        List<CreditCard> creditCards = creditCardService.findByClient(loggedUser.getId());
        List<String> creditCardsString = creditCards.stream().map(e -> e.getId().toString()).collect(Collectors.toList());
        model.addAttribute("databaseList", creditCardsString);
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        return "productOrderMenu";
    }

    @RequestMapping(value = "/backToClientMenu", method = RequestMethod.GET)
    public String backToMenu(Model model) {
        return "redirect:/clientMenu";
    }

    @RequestMapping(value = "/creditCardMenu", params = "addCreditCard", method = RequestMethod.POST)
    public String addCreditCard(Model model, @ModelAttribute("creditCardDTO") CreditCardDTO creditCardDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        Notification<Boolean> creditCardNotification = creditCardService.addCreditCard(loggedUser.getId(),
                creditCardDTO.getBalance(), creditCardDTO.getBankName(), creditCardDTO.getCardNr());
        if (creditCardNotification.hasErrors()) {
            model.addAttribute("notification", creditCardNotification.getFormattedErrors());
            return "creditCardMenu";
        } else {
            model.addAttribute("notification", "A new credit card was added successfully");
            return "creditCardMenu";
        }
    }

    @RequestMapping(value = "/creditCardMenu", params = "viewCreditCard", method = RequestMethod.POST)
    public String viewCreditCards(Model model) {
        model.addAttribute("creditCardDTO", new CreditCardDTO());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        List<CreditCard> creditCards = creditCardService.findByClient(loggedUser.getId());
        model.addAttribute("creditCards", creditCards);
        return "creditCardMenu";
    }

    @RequestMapping(value = "/productOrderMenu", params = "addToCart", method = RequestMethod.POST)
    public String addToCart(Model model, @ModelAttribute("productOrderDTO") ProductOrderDTO productOrderDTO, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        HttpSession session = request.getSession();
        Product product = productService.findById(productOrderDTO.getProductId());
        ProductOrder productOrder = new ProductOrderBuilder().setProduct(product).setClient(loggedUser).setQuantity(productOrderDTO.getQuantity()).build();
        List<ProductOrder> productOrders = (List<ProductOrder>) session.getAttribute("cartList");
        if (productOrders == null) {
            productOrders = new ArrayList<>();
        }
        productOrders.add(productOrder);
        session.setAttribute("cartList", productOrders);
        model.addAttribute("notification", "New order was added to shopping cart");
        return "productOrderMenu";
    }

    @RequestMapping(value = "/productOrderMenu", params = "viewShoppingCart", method = RequestMethod.POST)
    public String viewCart(Model model, HttpServletRequest request) {
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        HttpSession session = request.getSession();
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        model.addAttribute("productOrders", productOrders);
        return "productOrderMenu";
    }

    @RequestMapping(value = "/productOrderMenu", params = "finalizeCommand", method = RequestMethod.POST)
    public String finalizeCommand(Model model, HttpServletRequest request,@RequestParam("dropOperator") String creditCardIdd) {
        System.out.println(creditCardIdd.length());
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        HttpSession session = request.getSession();
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        double totalAmountToBePaid = 0;
        for(ProductOrder productOrder:productOrders)
        {
            totalAmountToBePaid += productOrder.getQuantity() * productOrder.getProduct().getPrice();
        }
        long creditCardId=Long.parseLong(creditCardIdd);
        CreditCard creditCard=creditCardService.findById(creditCardId);
        System.out.println("Balance before"+creditCard.getBalance());
        if(totalAmountToBePaid<creditCard.getBalance())
        {
            Notification<Boolean> creditCardNotification=creditCardService.updateBalance(creditCard.getId(),totalAmountToBePaid);
        }
        CreditCard updatedCreditCard=creditCardService.findById(creditCardId);
        System.out.println("Balance after"+updatedCreditCard.getBalance());

        String allErrors = "";
        Notification<CommandOrder> commandOrderNotification=
                commandOrderService.addCommandOrder(new java.sql.Date(System.currentTimeMillis()),"FANCourier");
        CommandOrder commandOrder=commandOrderNotification.getResult();
        for (ProductOrder productOrder : productOrders) {
            Notification<Boolean> orderNotification = productOrderService.addProductOrder(productOrder.getProduct().getId(),
                    productOrder.getClient().getId(),commandOrder.getId(),
                    productOrder.getQuantity());
            Notification<Boolean> productNotification = productService.updateProductStock(
                    productOrder.getProduct().getId(),
                    productOrder.getProduct().getStock() - productOrder.getQuantity());
            if (orderNotification.hasErrors()) allErrors += orderNotification.getFormattedErrors();
            else if (productNotification.hasErrors())
                allErrors += productNotification.getFormattedErrors();
        }

        if (!allErrors.equals("")) model.addAttribute("notification", allErrors);
        else {
            String formatString="Shopping cart was processed successfully " +
                    " Total amount to be paid was " + totalAmountToBePaid +
                    " Money left on the credit card "+updatedCreditCard.getBalance() +
                    " Generated Id if you want to track your command order is: "+commandOrder.getId();
            model.addAttribute("AlertMessage", formatString);
            List<ProductOrder> productOrders1 = new ArrayList<>();
            session.setAttribute("cartList", productOrders1);
        }

        return "productOrderMenu";
    }

    @RequestMapping(value = "/ratingMenu", params = "addRating", method = RequestMethod.POST)
    public String addRating(Model model, @RequestParam("rating") int value, @RequestParam("description") String description,
                            @RequestParam("name") String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        System.out.println(value);
        Notification<Boolean> ratingNotification = ratingService.addRating(loggedUser, name, value, description);
        if (ratingNotification.hasErrors()) {
            model.addAttribute("notification", ratingNotification.getFormattedErrors());
        } else {
            model.addAttribute("notification", "Review was added successfully");
        }
        return "ratingMenu";
    }

    @RequestMapping(value = "/ratingMenu", params = "viewRatings", method = RequestMethod.POST)
    public String viewYourRatings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        List<Rating> ratings = ratingService.findByClient(loggedUser);
        model.addAttribute("reviews", ratings);
        return "ratingMenu";
    }

    @RequestMapping(value = "/trackOrder",params = "trackOrderBtn",method = RequestMethod.POST)
    public String trackYourOrder(Model model,@RequestParam("idOrderTrack") long orderId)
    {
        CommandOrder commandOrder=commandOrderService.findById(orderId);
        List<ProductOrder> productOrders=productOrderService.findByCommandOrder(commandOrder);
        model.addAttribute("AlertMessageTrack"," Your command is scheduled to be delivered at "
                +commandOrder.getExpectedArrivalDate() + " Delivered by " +commandOrder.getDeliveryCompany()
                +" You can see your products below: ");
        model.addAttribute("productOrders",productOrders);
        return "/trackOrder";
    }

}
