package ua.com.vetal.report.jasperReport.exporter;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;

@Slf4j
public class JasperReportFactory {
	public JasperReportExporter getJasperReport(JasperReportExporterType type, JasperPrint jasperPrint) {
		log.info("Get JasperReportExporter for type: {}", type);
		JasperReportExporter toReturn = null;
		switch (type) {
			case XLS:
				toReturn = new JasperReportExporterXLS(jasperPrint);
				break;
			case XLSX:
				toReturn = new JasperReportExporterXLSX(jasperPrint);
				break;
			case PDF:
				toReturn = new JasperReportExporterPDF(jasperPrint);
				break;
			case X_PDF:
				toReturn = new JasperReportExporterXPDF(jasperPrint);
				break;
			default:
				throw new IllegalArgumentException("Wrong JasperReportExporter type:" + type);
		}
		return toReturn;
	}
}
