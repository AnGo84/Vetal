package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ContractorJasperReportData implements Reportable<Contractor, JasperReportData, PersonFilter> {
	@Override
	public JasperReportData getReportData(Contractor object) {
		log.info("Get JasperReportData for: {}", object);
		return null;
	}

	@Override
	public JasperReportData getReportData(List<Contractor> objects) {
		log.info("Get JasperReportData for objects: {}", objects);

		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("contractors", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.CONTRACTORS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Contractor> objects, PersonFilter filterData) {
		log.info("Get JasperReportData for objects: {}", objects);
		log.info("Filter: {}", filterData);

		return getReportData(objects);
	}
}
