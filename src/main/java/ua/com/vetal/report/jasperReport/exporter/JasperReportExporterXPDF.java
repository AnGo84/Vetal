package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JasperPrint;

public class JasperReportExporterXPDF extends AbstractPDFJasperReportExporter {
	public JasperReportExporterXPDF(JasperPrint jasperPrint) {
		this.type = JasperReportExporterType.X_PDF;
		this.jasperPrint = jasperPrint;
	}

}
