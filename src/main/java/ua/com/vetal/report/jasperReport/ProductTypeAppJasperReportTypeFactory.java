package ua.com.vetal.report.jasperReport;

import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.entity.ProductionTypeDirectory;

@Slf4j
public class ProductTypeAppJasperReportTypeFactory {
	public AppJasperReportType getAppJasperReport(ProductionTypeDirectory productionType) {
		log.info("Get AppJasperReport for ProductionType: {}", productionType);
		AppJasperReportType toReturn = null;
		int productionTypeId = productionType.getId().intValue();
		switch (productionTypeId) {
			case 2:
				toReturn = AppJasperReportType.TASK_LARGE_FORMAT_PRINTING_REPORT;
				break;
			case 3:
			case 4:
				toReturn = AppJasperReportType.TASK_SOUVENIR_REPORT;
				break;
			case 5:
			case 6:
				toReturn = AppJasperReportType.TASK_OFFSET_AND_DIGITAL_REPORT;
				break;
			default:
				toReturn = AppJasperReportType.TASK_REPORT;
		}
		return toReturn;

	}
}
