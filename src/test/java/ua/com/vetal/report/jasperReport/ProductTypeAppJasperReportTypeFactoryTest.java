package ua.com.vetal.report.jasperReport;

import org.junit.jupiter.api.Test;
import ua.com.vetal.entity.ProductionTypeDirectory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductTypeAppJasperReportTypeFactoryTest {
	private ProductTypeAppJasperReportTypeFactory typeFactory = new ProductTypeAppJasperReportTypeFactory();

	@Test
	public void whenGetAppJasperReportReturnResult() {
		ProductionTypeDirectory productionType = new ProductionTypeDirectory();
		productionType.setId(2l);
		AppJasperReportType appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_LARGE_FORMAT_PRINTING_REPORT, appJasperReportType);

		productionType.setId(3l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_SOUVENIR_REPORT, appJasperReportType);
		productionType.setId(4l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_SOUVENIR_REPORT, appJasperReportType);

		productionType.setId(5l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_OFFSET_AND_DIGITAL_REPORT, appJasperReportType);
		productionType.setId(6l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_OFFSET_AND_DIGITAL_REPORT, appJasperReportType);

		productionType.setId(-1l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_REPORT, appJasperReportType);
		productionType.setId(10l);
		appJasperReportType = typeFactory.getAppJasperReport(productionType);
		assertEquals(AppJasperReportType.TASK_REPORT, appJasperReportType);
	}

	@Test
	public void whenGetAppJasperReportReturnNull() {
		AppJasperReportType appJasperReportType = typeFactory.getAppJasperReport(null);
		assertNull(appJasperReportType);

		appJasperReportType = typeFactory.getAppJasperReport(new ProductionTypeDirectory());
		assertNull(appJasperReportType);
	}

}