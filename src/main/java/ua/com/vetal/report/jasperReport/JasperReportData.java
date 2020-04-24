package ua.com.vetal.report.jasperReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JasperReportData {
	private String reportName;
	private Map<String, Object> parameters;
	private JRRewindableDataSource dataSource;
}
