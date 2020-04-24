package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JasperReportExporterXLS extends JasperReportExporter {
	public JasperReportExporterXLS(JasperPrint jasperPrint) {
		this.type = JasperReportExporterType.XLS;
		this.jasperPrint = jasperPrint;
	}

	@Override
	public void exportToResponseStream(String outputFileName, HttpServletResponse response) throws JRException, IOException {
		// Create a JRXlsExporter instance
		JRXlsExporter exporter = new JRXlsExporter();
		// Excel specific parameters
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getOutputStream(outputFileName, response)));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setWhitePageBackground(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}

	@Override
	public ByteArrayOutputStream exportToStream() throws JRException, IOException {
		return null;
	}
}
