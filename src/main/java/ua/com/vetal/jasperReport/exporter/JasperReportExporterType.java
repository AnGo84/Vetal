package ua.com.vetal.jasperReport.exporter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum JasperReportExporterType {
    PDF(JasperReportMediaType.X_PDF, "pdf"),
    XLS(JasperReportMediaType.EXCEL, "xls"),
    XLSX(JasperReportMediaType.EXCEL, "xlsx");

    private JasperReportMediaType mediaType;
    private String fileExtension;
}
