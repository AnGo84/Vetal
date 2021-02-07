package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ClientJasperReportData implements Reportable<Client, JasperReportData, ContragentViewFilter> {
	@Override
	public JasperReportData getReportData(Client object) {
		log.info("Get JasperReportData for: {}", object);
		return null;
	}

	@Override
	public JasperReportData getReportData(List<Client> objects) {
		log.info("Get JasperReportData for objects: {}", objects);

		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("clients", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.CLIENTS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Client> objects, ContragentViewFilter filterData) {
		log.info("Get JasperReportData for objects: {}", objects);
		log.info("Filter: {}", filterData);

		return getReportData(objects);
	}
}
