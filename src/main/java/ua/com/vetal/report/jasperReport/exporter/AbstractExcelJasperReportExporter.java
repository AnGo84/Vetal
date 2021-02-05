package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.export.AbstractXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractExcelJasperReportExporter extends JasperReportExporter {

    @Override
    public ByteArrayOutputStream exportToStream() throws JRException, IOException {
        return null;
    }

    protected JRXlsAbstractExporter initJRXlsExporter(JRXlsAbstractExporter exporter, AbstractXlsReportConfiguration reportConfiguration, OutputStream outputStream) {
        // Excel specific parameters
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        exporter.setConfiguration(fillConfiguration(new SimpleXlsxReportConfiguration()));
        return exporter;
    }

    private AbstractXlsReportConfiguration fillConfiguration(AbstractXlsReportConfiguration reportConfiguration) {
        reportConfiguration.setOnePagePerSheet(true);
        reportConfiguration.setDetectCellType(true);
        reportConfiguration.setCollapseRowSpan(false);
        reportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
        reportConfiguration.setWhitePageBackground(false);
        return reportConfiguration;
    }
}
