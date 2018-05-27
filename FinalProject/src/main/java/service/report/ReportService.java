package service.report;

import model.Product;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    void generateReport(List<Product> limitedStockProducts) throws IOException;
}
