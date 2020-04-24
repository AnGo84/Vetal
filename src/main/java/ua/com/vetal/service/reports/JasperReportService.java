package ua.com.vetal.service.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.report.jasperReport.JasperPrintData;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporter;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.exporter.JasperReportFactory;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class JasperReportService {

	@Autowired
	private JasperPrintData jasperPrintData;

	private JasperReportFactory jasperReportFactory = new JasperReportFactory();

	public void export(JasperReportExporterType type,
					   JasperReportData jasperReportData,
					   String outputFileName,
					   HttpServletResponse response) throws JRException, IOException {
		log.info("Export as response stream file '{}' with type '{}'", outputFileName, type);
		JasperPrint jasperPrint = jasperPrintData.getJasperPrint(jasperReportData);
		JasperReportExporter jasperReportExporter =
				jasperReportFactory.getJasperReport(type, jasperPrint);

		jasperReportExporter.exportToResponseStream(outputFileName, response);
	}

	public EmailAttachment getEmailAttachment(
			JasperReportExporterType type,
			String attachmentFullName,
			JasperReportData jasperReportData) throws JRException, IOException {
		log.info("Export as EmailAttachment file '{}' with type '{}'", attachmentFullName, type);
		JasperPrint jasperPrint = jasperPrintData.getJasperPrint(jasperReportData);
		JasperReportExporter jasperReportExporter =
				jasperReportFactory.getJasperReport(type, jasperPrint);

		ByteArrayOutputStream baos = jasperReportExporter.exportToStream();
		DataSource dataSource = new ByteArrayDataSource(baos.toByteArray(), jasperReportExporter.getMediaType());
		return new EmailAttachment(attachmentFullName, dataSource);
	}
}
