package controller;

import model.ProductOrder;
import model.User;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.productOrder.ProductOrderService;
import service.user.UserService;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductOrderService productOrderService;
    //User operations
    @RequestMapping(value = "/userOps", method = RequestMethod.GET)
    public String showUserOps(Model model) {
        return "userOps";
    }

    @RequestMapping(value = "/userOps", params = "viewUser", method = RequestMethod.POST)
    public String viewUsers(Model model) {
        final List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "userOps";
    }

    @RequestMapping(value = "/userOps", params = "addUser", method = RequestMethod.POST)
    public String addEmployee(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                              @RequestParam("role") String role) {
        Notification<Boolean> userNotification = userService.addUser(username, password, role);
        if (userNotification.hasErrors()) {
            model.addAttribute("error", userNotification.getFormattedErrors());
            return "/userOps";
        } else {
            model.addAttribute("addOk", "New employee added successfully");
            return "/userOps";
        }
    }

    @RequestMapping(value = "/userOps", params = "updateUser", method = RequestMethod.POST)
    public String updateUser(Model model, @RequestParam("idUser") String idUser, @RequestParam("username") String username,
                             @RequestParam("password") String password, @RequestParam("role") String role) {
        Long id = Long.parseLong(idUser);
        Notification<Boolean> userNotification = userService.updateUser(id, username, password, role);
        if (userNotification.hasErrors()) {
            model.addAttribute("updateError", userNotification.getFormattedErrors());
            return "/userOps";
        } else {
            model.addAttribute("updateOk", "User was updated successfully");
            return "/userOps";
        }

    }

    @RequestMapping(value = "/userOps", params = "deleteUser", method = RequestMethod.POST)
    public String deleteUser(Model model, @RequestParam("idUser") String idUser) {
        Long id = Long.parseLong(idUser);
        userService.deleteUser(id);
        model.addAttribute("deleteMessage", "User with id " + id + " was deleted succesfully from the database");
        return "/userOps";
    }

    @RequestMapping(value = "/userOps",params = "trackUser",method = RequestMethod.POST)
    public String trackClient(Model model,@RequestParam("userTrack")String userTrack)
    {
        List<User> users=userService.findByUsername(userTrack);
        User user=users.get(0);
        List<ProductOrder> productOrders=productOrderService.findByClient(user);
        model.addAttribute("productOrders",productOrders);
        return "/userOps";
    }
}
