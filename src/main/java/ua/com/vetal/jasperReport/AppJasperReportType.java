package ua.com.vetal.jasperReport;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppJasperReportType {
	TASK_REPORT("/jasperReport/TaskReport.jasper"),
	TASK_LARGE_FORMAT_PRINTING_REPORT("/jasperReport/Task_Large_Format_Printing_Report.jasper"),
	TASK_SOUVENIR_REPORT("/jasperReport/Task_Souvenir_Report.jasper"),
	TASK_OFFSET_AND_DIGITAL_REPORT("/jasperReport/Task_Offset_and_Digital_Report.jasper");

	private String reportName;

}
