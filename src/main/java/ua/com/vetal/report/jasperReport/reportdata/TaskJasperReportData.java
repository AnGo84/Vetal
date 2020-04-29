package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.ProductTypeAppJasperReportTypeFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TaskJasperReportData implements Reportable<Task, JasperReportData, FilterData> {
	@Override
	public JasperReportData getReportData(Task object) {
		log.info("Get JasperReportData for: {}", object);
		ProductTypeAppJasperReportTypeFactory reportTypeFactory = new ProductTypeAppJasperReportTypeFactory();
		List<Task> objects = Arrays.asList(object);
		return JasperReportData.builder()
				.reportName(reportTypeFactory.getAppJasperReport(object.getProductionType()).getReportName())
				.parameters(null)
				.dataSource(new JRBeanCollectionDataSource(objects))
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Task> objects) {
		log.info("Get JasperReportData for objects: {}", objects);
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("tasks", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.TASKS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Task> objects, FilterData filterData) {
		return getReportData(objects);
	}
}
