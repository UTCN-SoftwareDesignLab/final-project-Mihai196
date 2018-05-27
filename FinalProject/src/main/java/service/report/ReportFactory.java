package service.report;

import org.springframework.stereotype.Service;

@Service
public class ReportFactory {

    public ReportService getReportType(String reportType)
    {
        if(reportType.equals("csv"))
        {
            return new CSVReportService();
        }
        if(reportType.equals("pdf"))
        {
            return new PDFReportService();
        }
        return null;
    }
}
