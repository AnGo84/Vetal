package ua.com.vetal.jasperReport.exporter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JasperReportMediaType {
	EXCEL("application/vnd.ms-excel"),
	PDF("application/pdf"),
	X_PDF("application/x-pdf");

	private String mediaType;

}
