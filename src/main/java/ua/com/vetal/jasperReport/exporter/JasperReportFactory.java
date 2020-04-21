package ua.com.vetal.jasperReport.exporter;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import ua.com.vetal.entity.ReportType;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JasperReportFactory {
	public JasperReportExporter getJasperReport(ReportType type,
												JasperPrint jasperPrint,
												String name,
												HttpServletResponse response) {
		log.info("Get JasperReportExporter for type: {}", type);
		JasperReportExporter toReturn = null;
		switch (type) {
			case XLS:
				toReturn = new JasperReportExporterXLS(jasperPrint, name, response);
				break;
			case XLSX:
				toReturn = new JasperReportExporterXLSX(jasperPrint, name, response);
				break;
			case PDF:
				toReturn = new JasperReportExporterPDF(jasperPrint, name, response);
				break;
			default:
				throw new IllegalArgumentException("Wrong JasperReportExporter type:" + type);
		}
		return toReturn;
	}
}
