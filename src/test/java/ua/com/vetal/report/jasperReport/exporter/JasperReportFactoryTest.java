package ua.com.vetal.report.jasperReport.exporter;

import net.sf.jasperreports.engine.JasperPrint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JasperReportFactoryTest {
	private JasperReportFactory jasperReportFactory = new JasperReportFactory();

	@Test
	public void whenGetJasperReportReturnResult() {
		JasperPrint jasperPrint = new JasperPrint();
		JasperReportExporter appJasperReportType = jasperReportFactory.getJasperReport(JasperReportExporterType.XLS, jasperPrint);
		assertEquals(JasperReportExporterXLS.class, appJasperReportType.getClass());

		appJasperReportType = jasperReportFactory.getJasperReport(JasperReportExporterType.XLSX, jasperPrint);
		assertEquals(JasperReportExporterXLSX.class, appJasperReportType.getClass());

		appJasperReportType = jasperReportFactory.getJasperReport(JasperReportExporterType.PDF, jasperPrint);
		assertEquals(JasperReportExporterPDF.class, appJasperReportType.getClass());

		appJasperReportType = jasperReportFactory.getJasperReport(JasperReportExporterType.X_PDF, jasperPrint);
		assertEquals(JasperReportExporterXPDF.class, appJasperReportType.getClass());

	}

	@Test
	public void whenGetJasperReportReturnNull() {
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			jasperReportFactory.getJasperReport(null, null);
		});
		assertTrue(thrown.getMessage().startsWith("Wrong JasperReportExporter type:"));
	}

}