package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrderJasperReportData implements Reportable<Order, JasperReportData, FilterData> {
	@Override
	public JasperReportData getReportData(Order object) {
		log.info("Get JasperReportData for: {}", object);
		return null;
	}

	@Override
	public JasperReportData getReportData(List<Order> objects) {
		log.info("Get JasperReportData for objects: {}", objects);

		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("orders", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.STENCILS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Order> objects, FilterData filterData) {
		log.info("Get JasperReportData for objects: {}", objects);
		log.info("Filter: {}", filterData);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("filter", filterData);

		String date = "";
		if (filterData.getDateBeginFrom() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginFrom());
		}
		parameters.put("date_From", date);
		date = "";
		if (filterData.getDateBeginTill() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginTill());
		}
		parameters.put("date_Till", date);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("orders", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.ORDERS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}
}
