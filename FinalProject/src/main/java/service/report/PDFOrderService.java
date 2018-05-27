package service.report;

import model.Product;
import model.ProductOrder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PDFOrderService implements ReportOrderService {

    @Override
    public void generateReport(String date, String deliveryProducts, List<ProductOrder> productOrders) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            String booksToBeAdded = "Printing Format:  ";
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 685);
            contentStream.showText(booksToBeAdded);
            contentStream.newLine();
            contentStream.showText(date);
            contentStream.newLine();
            contentStream.showText(deliveryProducts);
            contentStream.newLine();
            contentStream.newLine();
            for (ProductOrder productOrder : productOrders) {
                contentStream.showText(productOrder.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            document.save("ReportOrder.pdf");
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
