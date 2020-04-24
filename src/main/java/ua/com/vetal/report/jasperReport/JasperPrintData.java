package ua.com.vetal.report.jasperReport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class JasperPrintData {

	@Value("${image.logo}")
	private String imageLogo;

	public JasperPrint getJasperPrint(JasperReportData jasperReportData) throws JRException {
		log.info("Jasper Report Data: {}", jasperReportData);
		return getJasperPrint(jasperReportData.getReportName(), jasperReportData.getParameters(), jasperReportData.getDataSource());
	}

	public JasperPrint getJasperPrint(String reportName, Map<String, Object> parameters, JRRewindableDataSource dataSource) throws JRException {
		log.info("Jasper Report '{}' with params: {}", reportName, parameters);
		InputStream jasperStream = this.getClass().getResourceAsStream(reportName);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		log.info("JasperStream is null: " + (jasperStream == null));

		if (parameters == null) {
			parameters = new HashMap<>();
		}
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		parameters.put("paramLOGO", logoIS);

		return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	}

}
