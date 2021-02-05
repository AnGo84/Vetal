package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JasperPrint;

public class JasperReportExporterPDF extends AbstractPDFJasperReportExporter {
	public JasperReportExporterPDF(JasperPrint jasperPrint) {
		this.type = JasperReportExporterType.PDF;
		this.jasperPrint = jasperPrint;
	}

}
