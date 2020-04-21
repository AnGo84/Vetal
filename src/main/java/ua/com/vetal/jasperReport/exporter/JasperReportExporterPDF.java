package ua.com.vetal.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JasperReportExporterPDF extends JasperReportExporter {
	public JasperReportExporterPDF(JasperPrint jasperPrint,
								   String name,
								   HttpServletResponse response) {
		this.type = JasperReportExporterType.PDF;
		this.jasperPrint = jasperPrint;
		this.name = name;
		this.response = response;
	}

	@Override
	public void exportToStream() throws JRException, IOException {
		JasperExportManager.exportReportToPdfStream(jasperPrint, getOutputStream());
	}
}
