package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JasperReportExporterXLSX extends AbstractExcelJasperReportExporter {
    public JasperReportExporterXLSX(JasperPrint jasperPrint) {
        this.type = JasperReportExporterType.XLSX;
        this.jasperPrint = jasperPrint;
    }

    @Override
    public void exportToResponseStream(String outputFileName,
                                       HttpServletResponse response) throws JRException, IOException {
        // Create a JRXlsExporter instance
        JRXlsAbstractExporter exporter = initJRXlsExporter(new JRXlsxExporter(),
                new SimpleXlsxReportConfiguration(), getOutputStream(outputFileName, response));
        exporter.exportReport();
    }

}
