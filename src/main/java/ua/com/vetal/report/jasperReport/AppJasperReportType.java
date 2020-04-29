package ua.com.vetal.report.jasperReport;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppJasperReportType {
	TASK_REPORT("/jasperReport/TaskReport.jasper"),
	TASK_LARGE_FORMAT_PRINTING_REPORT("/jasperReport/Task_Large_Format_Printing_Report.jasper"),
	TASK_SOUVENIR_REPORT("/jasperReport/Task_Souvenir_Report.jasper"),
	TASK_OFFSET_AND_DIGITAL_REPORT("/jasperReport/Task_Offset_and_Digital_Report.jasper"),
	TASKS_REPORT("/jasperReport/TaskTableReport.jasper"),
	STENCIL_REPORT("/jasperReport/StencilReport.jasper"),
	STENCILS_REPORT("/jasperReport/StencilTableReport.jasper"),
	ORDERS_REPORT("/jasperReport/OrdersCrossReport.jasper"),
	CLIENTS_REPORT("/jasperReport/ClientsTableReport.jasper"),
	CONTRACTORS_REPORT("/jasperReport/ContractorsTableReport.jasper");

	private String reportName;

}
