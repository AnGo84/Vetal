package ua.com.vetal.report.jasperReport.reportdata;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.Reportable;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StencilJasperReportData implements Reportable<Stencil, JasperReportData, FilterData> {
	@Override
	public JasperReportData getReportData(Stencil object) {
		log.info("Get JasperReportData for: {}", object);
		List<Stencil> objects = Arrays.asList(object);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.STENCIL_REPORT.getReportName())
				.parameters(null)
				.dataSource(new JRBeanCollectionDataSource(objects))
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Stencil> objects) {
		log.info("Get JasperReportData for objects: {}", objects);
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
		parameters.put("stencils", dataSource);

		return JasperReportData.builder()
				.reportName(AppJasperReportType.STENCILS_REPORT.getReportName())
				.parameters(parameters)
				.dataSource(new JREmptyDataSource())
				.build();
	}

	@Override
	public JasperReportData getReportData(List<Stencil> objects, FilterData filterData) {
		return null;
	}
}
