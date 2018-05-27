package service.report;

import model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReportService implements ReportService {
    @Override
    public void generateReport(List<Product> limitedStockProducts) throws IOException {
        //List<Product> limitedStockProducts=products.stream().filter(e->e.getStock()<5).collect(Collectors.toList());
        try {
            PrintWriter pw = new PrintWriter(new File("Report.csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("ProductId");
            sb.append(',');
            sb.append("Name");
            sb.append(',');
            sb.append("SportCategory");
            sb.append(',');
            sb.append("TypeCategory");
            sb.append(',');
            sb.append("Stock");
            sb.append(',');
            sb.append("Price");
            sb.append('\n');
            for (Product product : limitedStockProducts) {
                sb.append(product.getId());
                sb.append(',');
                sb.append(product.getName());
                sb.append(',');
                sb.append(product.getSportCategory());
                sb.append(',');
                sb.append(product.getTypeCategory());
                sb.append(',');
                sb.append(product.getStock());
                sb.append(',');
                sb.append(product.getPrice());
                sb.append('\n');

            }
            pw.write(sb.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
