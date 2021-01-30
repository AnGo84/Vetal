package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.StatisticOrder;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrderJasperReportData implements Reportable<StatisticOrder, JasperReportData, OrderViewFilter> {
	@Override
	public JasperReportData getReportData(StatisticOrder object) {
		log.info("Get JasperReportData for: {}", object);
		return null;
	}

	@Override
	public JasperReportData getReportData(List<StatisticOrder> objects) {
		log.info("Get JasperReportData for objects: {}", objects);

		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("orders", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.ORDERS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<StatisticOrder> objects, OrderViewFilter orderViewFilter) {
		log.info("Get JasperReportData for objects: {}", objects);
		log.info("Filter: {}", orderViewFilter);
		if (orderViewFilter == null) {
			orderViewFilter = new OrderViewFilter();
		}
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("filter", orderViewFilter);

		String date = "";
		if (orderViewFilter.getDateBeginFrom() != null) {
            date = DateUtils.SIMPLE_DATE_FORMAT.format(orderViewFilter.getDateBeginFrom());
        }
        parameters.put("date_From", date);
        date = "";
        if (orderViewFilter.getDateBeginTill() != null) {
            date = DateUtils.SIMPLE_DATE_FORMAT.format(orderViewFilter.getDateBeginTill());
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
