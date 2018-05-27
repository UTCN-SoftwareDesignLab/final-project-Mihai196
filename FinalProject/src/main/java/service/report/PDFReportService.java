package service.report;

import model.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class PDFReportService implements ReportService {
    @Override
    public void generateReport(List<Product> limitedStockProducts) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            String booksToBeAdded = "Limited stock products are:  ";
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 685);
            contentStream.showText(booksToBeAdded);
            contentStream.newLine();
            for (Product product : limitedStockProducts) {
                contentStream.showText(product.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            document.save("Report.pdf");
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
