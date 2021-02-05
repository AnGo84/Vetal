package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JasperReportExporterXLS extends AbstractExcelJasperReportExporter {
	public JasperReportExporterXLS(JasperPrint jasperPrint) {
		this.type = JasperReportExporterType.XLS;
		this.jasperPrint = jasperPrint;
	}

	@Override
	public void exportToResponseStream(String outputFileName,
									   HttpServletResponse response) throws JRException, IOException {
		// Create a JRXlsExporter instance
		JRXlsAbstractExporter exporter = initJRXlsExporter(new JRXlsExporter(),
				new SimpleXlsReportConfiguration(), getOutputStream(outputFileName, response));
		exporter.exportReport();
	}
}
