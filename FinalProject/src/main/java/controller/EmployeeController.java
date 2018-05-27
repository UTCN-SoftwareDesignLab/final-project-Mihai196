package controller;

import dto.ProductDTO;
import model.Product;
import model.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.product.ProductService;
import service.report.ReportFactory;
import service.report.ReportService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ResourceUtils.getFile;

@Controller
public class EmployeeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportFactory reportFactory;

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
        final List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "redirect:/employee";
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
            return "redirect:/employee";
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
    public String deleteProduct(Model model,@ModelAttribute("productDTO") ProductDTO productDTO) {
        productService.deleteProduct(productDTO.getId());
        model.addAttribute("notification", "Product was deleted successfully");
        return "employee";
    }
    @RequestMapping(value = "/employee",params = "generatePDF",method = RequestMethod.POST)
    public @ResponseBody HttpEntity<byte[]> generatePDFReport(Model model) throws IOException {
        ReportService pdfReportService= reportFactory.getReportType("pdf");
        List<Product> products=productService.findAllProducts();
        List<Product> limitedStockProducts=products.stream().filter(e->e.getStock()<5).collect(Collectors.toList());
        pdfReportService.generateReport(limitedStockProducts);
        System.out.println(limitedStockProducts.get(0).toString());
        String filePath="E:/Mihaica/Faculta/An3/Sem2/SD/final-project-Mihai196/FinalProject/Report.pdf";
        File file=getFile(filePath);
        byte[] document=FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);
        model.addAttribute("notification","Report was generated and downloaded successfully");

        return new HttpEntity<byte[]>(document, header);
    }
    @RequestMapping(value = "/employee",params = "generateCSV",method = RequestMethod.POST)
    public @ResponseBody HttpEntity<byte[]> generateCSVReport(Model model) throws IOException {
        ReportService csvReportService= reportFactory.getReportType("csv");
        List<Product> products=productService.findAllProducts();
        List<Product> limitedStockProducts=products.stream().filter(e->e.getStock()<5).collect(Collectors.toList());
        csvReportService.generateReport(limitedStockProducts);
        String filePath="E:/Mihaica/Faculta/An3/Sem2/SD/final-project-Mihai196/FinalProject/Report.csv";
        File file=getFile(filePath);
        byte[] document=FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "csv"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
        header.setContentLength(document.length);
        model.addAttribute("notification","Report was generated and downloaded successfully");

        return new HttpEntity<byte[]>(document, header);
    }

}
