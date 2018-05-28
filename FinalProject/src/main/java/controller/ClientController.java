package controller;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Thumbnail;
import dto.CreditCardDTO;
import dto.ProductOrderDTO;
import dto.SearchYoutube;
import model.*;
import model.builder.ProductOrderBuilder;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import service.commandOrder.CommandOrderService;
import service.creditCard.CreditCardService;
import service.product.ProductService;
import service.productOrder.ProductOrderService;
import service.rating.RatingService;
import service.report.ReportFactory;
import service.report.ReportOrderService;
import service.report.ReportService;
import service.user.UserService;
import service.youtubeAPI.YoutubeService;

import com.google.api.services.youtube.model.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ResourceUtils.getFile;

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

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private ReportFactory reportFactory;

    @Autowired
    private ReportOrderService reportOrderService;

    @RequestMapping(value = "/clientMenu", method = RequestMethod.GET)
    public String showUserOps(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (productOrders!=null) {
            if(productOrders.size()==0)
            {
                model.addAttribute("AlertMessage", "");
            }
            else {
                model.addAttribute("AlertMessage", "You have not finalized your command." +
                        "Make sure you finalize it before you log out.");
            }
        }
        else
        {
            model.addAttribute("AlertMessage", "");
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
    public String showProductMenu(Model model,HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        List<CreditCard> creditCards = creditCardService.findByClient(loggedUser.getId());
        List<String> creditCardsString = creditCards.stream().map(e -> e.getId().toString()).collect(Collectors.toList());
        model.addAttribute("databaseList", creditCardsString);
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        HttpSession session = request.getSession();
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        model.addAttribute("productOrders", productOrders);
        return "productOrderMenu";
    }

    @RequestMapping(value = "/clearCart",params = "clearCart",method = RequestMethod.POST)
    public String clearCart(Model model,HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        List<ProductOrder> productOrders = (ArrayList<ProductOrder>) session.getAttribute("cartList");
        productOrders=new ArrayList<>();
        session.setAttribute("cartList",productOrders);
        return "redirect:/productOrderMenu";

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
    @RequestMapping(value = "/creditCardMenu", params = "deleteCreditCard", method = RequestMethod.POST)
    public String deleteCreditCard(Model model, @ModelAttribute("creditCardDTO") CreditCardDTO creditCardDTO) {
        model.addAttribute("creditCardDTO", new CreditCardDTO());
        creditCardService.deleteCreditCard(creditCardDTO.getId());
        model.addAttribute("notification","Credit card was deleted successfully");
        return "creditCardMenu";
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
        if(creditCard.getBalance()>totalAmountToBePaid) {
            System.out.println("Balance before" + creditCard.getBalance());
            if (totalAmountToBePaid < creditCard.getBalance()) {
                Notification<Boolean> creditCardNotification = creditCardService.updateBalance(creditCard.getId(), totalAmountToBePaid);
            }
            CreditCard updatedCreditCard = creditCardService.findById(creditCardId);
            System.out.println("Balance after" + updatedCreditCard.getBalance());

            String allErrors = "";
            Notification<CommandOrder> commandOrderNotification =
                    commandOrderService.addCommandOrder(new java.sql.Date(System.currentTimeMillis()+2*86400000), "FANCourier");
            CommandOrder commandOrder = commandOrderNotification.getResult();
            for (ProductOrder productOrder : productOrders) {
                Notification<Boolean> orderNotification = productOrderService.addProductOrder(productOrder.getProduct().getId(),
                        productOrder.getClient().getId(), commandOrder.getId(),
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
                String formatString = "Shopping cart was processed successfully " +
                        " Total amount to be paid was " + totalAmountToBePaid +
                        " Money left on the credit card " + updatedCreditCard.getBalance() +
                        " Generated Id if you want to track your command order is: " + commandOrder.getId();
                model.addAttribute("AlertMessage", formatString);
                List<ProductOrder> productOrders1 = new ArrayList<>();
                session.setAttribute("cartList", productOrders1);
            }
            return "productOrderMenu";
        }
        else
        {
            return "redirect:/productOrderMenu";
        }


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
    @RequestMapping(value="/ratingMenu",params = "viewRatingsProduct",method = RequestMethod.POST)
    public String viewRatingsProduct(Model model,@RequestParam("name") String prodName)
    {
        List<Rating> ratings=ratingService.findAllRatings();
        List<Rating> ratingsStream=ratings.stream().filter(e->e.getProduct().getName().equals(prodName)).collect(Collectors.toList());
        model.addAttribute("reviews", ratingsStream);
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

    @RequestMapping(value ="/productFilter",params = "filterBySport",method = RequestMethod.POST)
    public String filterBySport(Model model,@RequestParam("sportCategory")String sportCategory)
    {
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        List<Product> products=productService.findBySportCategory(sportCategory);
        model.addAttribute("products",products);
        return "productOrderMenu";
    }
    @RequestMapping(value ="/productFilter",params = "filterByType",method = RequestMethod.POST)
    public String filterByType(Model model,@RequestParam("typeCategory")String typeCategory)
    {
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        List<Product> products=productService.findByTypeCategory(typeCategory);
        model.addAttribute("products",products);
        return "productOrderMenu";
    }

    @RequestMapping(value = "/addFromTable", params="AddProduct",method = RequestMethod.POST)
    public String addToCartFromTable(Model model,@RequestParam("productIdInsert") long productId,HttpServletRequest request)
    {
        model.addAttribute("productOrderDTO", new ProductOrderDTO());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersByUsername = userService.findByUsername(auth.getName());
        User loggedUser = usersByUsername.get(0);
        HttpSession session = request.getSession();
        Product product = productService.findById(productId);
        ProductOrder productOrder = new ProductOrderBuilder().setProduct(product).setClient(loggedUser).setQuantity(1).build();
        List<ProductOrder> productOrders = (List<ProductOrder>) session.getAttribute("cartList");
        if (productOrders == null) {
            productOrders = new ArrayList<>();
        }
        if(product.getStock()>2) {
            productOrders.add(productOrder);
        }
        else
        {
            model.addAttribute("AlertMessage","Product is out of stock");
        }
        session.setAttribute("cartList", productOrders);
        return "redirect:/productOrderMenu";
    }

    @RequestMapping(value = "/productOrderMenu",params = "searchYoutubeVideos",method = RequestMethod.POST)
    public String showYoutubeSearch(Model model,@RequestParam("productName") String productName)
    {
        String queryTerm=productName+" review";
        List<SearchResult> searchResults=youtubeService.getSearchResult(queryTerm);
        Iterator<SearchResult> iteratorSearchResults=searchResults.iterator();
        List<SearchYoutube> searchYoutubes=new ArrayList<>();
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                String link="https://www.youtube.com/watch?v="+rId.getVideoId();
                String title=singleVideo.getSnippet().getTitle();
                SearchYoutube searchYoutube=new SearchYoutube(link,title);
                searchYoutubes.add(searchYoutube);
            }
        }
        for(SearchYoutube searchYoutube:searchYoutubes)
        {
            System.out.println(searchYoutube.getLink());
            System.out.println(searchYoutube.getTitle());
        }
        model.addAttribute("searchResults",searchYoutubes);
        return "/productOrderMenu";
    }
    @RequestMapping(value = "/trackOrder",params = "viewPrintFormat",method = RequestMethod.POST)
    public @ResponseBody HttpEntity<byte[]> generateOrderReport(Model model,@RequestParam("idOrderTrack") long orderId) throws IOException {
        CommandOrder commandOrder=commandOrderService.findById(orderId);
        List<ProductOrder> productOrders=productOrderService.findByCommandOrder(commandOrder);
        String date=" Your command is scheduled to be delivered at "
                +commandOrder.getExpectedArrivalDate();
        String deliveryProducts=" Delivered by " +commandOrder.getDeliveryCompany() +" You can see your products below: ";
        reportOrderService.generateReport(date,deliveryProducts,productOrders);
        String filePath="E:/Mihaica/Faculta/An3/Sem2/SD/final-project-Mihai196/FinalProject/ReportOrder.pdf";
        File file=getFile(filePath);
        byte[] document=FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);
        return new HttpEntity<byte[]>(document, header);
    }

}
