package controller;

import dto.ProductDTO;
import model.Product;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.product.ProductService;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String showPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        model.addAttribute("helloMessage","Hello " +auth.getName());
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("products", productService.findAllProducts());
        return "employee";
    }

    @RequestMapping(value = "/employee", params = "viewProducts", method = RequestMethod.POST)
    public String listProducts(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        final List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "employee";
    }

    @RequestMapping(value = "/employee", params = "addProduct", method = RequestMethod.POST)
    public String addProduct(Model model, @ModelAttribute("productDTO") ProductDTO productDTO) {
        Notification<Boolean> productNotification =
                productService.addProduct(productDTO.getName(), productDTO.getSportCategory(),productDTO.getTypeCategory(), productDTO.getDescription()
                        , productDTO.getStock(), productDTO.getPrice());
        if (productNotification.hasErrors()) {
            model.addAttribute("notification", productNotification.getFormattedErrors());
            return "employee";
        } else {
            model.addAttribute("notification", "New product was added successfully");
            return "employee";
        }
    }
    @RequestMapping(value = "/employee", params = "updateProduct", method = RequestMethod.POST)
    public String updateProduct(Model model, @ModelAttribute("productDTO") ProductDTO productDTO)
    {
        Notification<Boolean> productNotification=productService.updateProductDetails(
                productDTO.getId(),productDTO.getName(),productDTO.getSportCategory(),productDTO.getTypeCategory(),productDTO.getPrice());
        if (productNotification.hasErrors()) {
            model.addAttribute("notification", productNotification.getFormattedErrors());
            return "employee";
        } else {
            model.addAttribute("notification", "Product was updated successfully");
            return "employee";
        }

    }
    @RequestMapping(value = "/employee", params = "deleteProduct", method = RequestMethod.POST)
    public String deleteProduct(Model model,@ModelAttribute("productDTO") ProductDTO productDTO)
    {
        productService.deleteProduct(productDTO.getId());
        model.addAttribute("notification", "Product was deleted successfully");
        return "employee";
    }


}
