package ua.com.vetal.service.reports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.vetal.entity.ReportType;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class ExporterService {
    public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
    public static final String MEDIA_TYPE_PDF = "application/pdf";
    public static final String MEDIA_TYPE_X_PDF = "application/x-pdf";
    static final Logger logger = LoggerFactory.getLogger(ExporterService.class);

    //https://www.baeldung.com/spring-jasper

    //public HttpServletResponse export
    public void export(ReportType type,
                       JasperPrint jasperPrint,
                       String name,
                       HttpServletResponse response) throws JRException, IOException {
        response.setHeader("Content-Disposition", "inline; filename=" + name + "." + type.name().toLowerCase());
        //final OutputStream outputStream = response.getOutputStream();
        logger.info("Export to " + type);
        if (type.equals(ReportType.XLSX)) {
            logger.info("XLSX!");
            // Export to output stream
            //exportXlsx(jasperPrint, outputStream);
            // Set our response properties
            // Set content type
            response.setContentType(MEDIA_TYPE_EXCEL);
            //response.setContentLength(outputStream.size());
            final OutputStream outputStream = response.getOutputStream();
            exportXlsx(jasperPrint, outputStream);
            //return response;
        } else if (type.equals(ReportType.XLS)) {
            logger.info("XLS!");
            // Export to output stream
            //exportXls(jasperPrint, outputStream);
            // Set our response properties
            // Here you can declare a custom filename
            //response.setHeader("Content-Disposition", "inline; filename=" + name);
            // Set content type
            response.setContentType(MEDIA_TYPE_EXCEL);
            //response.setContentLength(outputStream.size());

            // Export to output stream
            exportXls(jasperPrint, response.getOutputStream());
            //return response;
        } else if (type.equals(ReportType.PDF)) {
            logger.info("PDF!");
            // Export to output stream
            //exportPdf(jasperPrint, outputStream);

            // Set our response properties
            // Here you can declare a custom filename

            // Set content type
            response.setContentType(MEDIA_TYPE_X_PDF);
            //response.setContentLength(outputStream.size());

            // Export to output stream
            final OutputStream outputStream = response.getOutputStream();
            exportPdf(jasperPrint, outputStream);
            //return response;

        } else {
            throw new RuntimeException("No report set for type " + type);
        }
    }

    public void exportXls(JasperPrint jasperPrint, OutputStream outputStream) {
        // Create a JRXlsExporter instance
        JRXlsExporter exporter = new JRXlsExporter();
        // for deprecated use same as for xlsx

        // Here we assign the parameters jp and baos to the exporter
        /*exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);*/

        // Excel specific parameters
        /*exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);*/

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);

        try {
            exporter.exportReport();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportXlsx(JasperPrint jasperPrint, OutputStream outputStream) {
        // Create a JRXlsxExporter instance
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);

        try {
            exporter.exportReport();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportPdf(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {

        //JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

    }

    public DataSource getDataSource(JasperPrint jasperPrint) throws JRException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        return aAttachment;
    }

}
