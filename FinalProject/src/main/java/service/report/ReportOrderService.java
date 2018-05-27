package service.report;

import model.Product;
import model.ProductOrder;

import java.io.IOException;
import java.util.List;

public interface ReportOrderService {

    void generateReport(String date, String deliveryProducts, List<ProductOrder> productOrders) throws IOException;
}
