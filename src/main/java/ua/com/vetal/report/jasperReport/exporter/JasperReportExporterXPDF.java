package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JasperReportExporterXPDF extends JasperReportExporter {
	public JasperReportExporterXPDF(JasperPrint jasperPrint) {
		this.type = JasperReportExporterType.X_PDF;
		this.jasperPrint = jasperPrint;
	}

	@Override
	public void exportToResponseStream(String outputFileName,
									   HttpServletResponse response) throws JRException, IOException {
		JasperExportManager.exportReportToPdfStream(jasperPrint, getOutputStream(outputFileName, response));
	}

	@Override
	public ByteArrayOutputStream exportToStream() throws JRException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
		return baos;
	}
}
