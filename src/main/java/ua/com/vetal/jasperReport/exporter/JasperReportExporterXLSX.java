package ua.com.vetal.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JasperReportExporterXLSX extends JasperReportExporter {
	public JasperReportExporterXLSX(JasperPrint jasperPrint,
									String name,
									HttpServletResponse response) {
		this.type = JasperReportExporterType.XLSX;
		this.jasperPrint = jasperPrint;
		this.name = name;
		this.response = response;
	}

	@Override
	public void exportToStream() throws JRException, IOException {
		// Create a JRXlsExporter instance
		JRXlsxExporter exporter = new JRXlsxExporter();
		// Excel specific parameters
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(getOutputStream()));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setWhitePageBackground(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}
}
