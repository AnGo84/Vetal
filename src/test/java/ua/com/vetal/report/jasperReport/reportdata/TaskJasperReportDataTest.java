package ua.com.vetal.report.jasperReport.reportdata;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TaskJasperReportDataTest {
	private TaskJasperReportData reportData = new TaskJasperReportData();

	@Test
	public void whenGetReportDataFromTaskReturnResult() {
		Task task = TestDataUtils.getTask(1l, 1);
		JasperReportData jasperReportData = reportData.getReportData(task);
		assertEquals(AppJasperReportType.TASK_REPORT.getReportName(), jasperReportData.getReportName());
		assertNull(jasperReportData.getParameters());
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JRBeanCollectionDataSource.class);
	}


	@Test
	public void whenGetReportDataFromListReturnResult() {
		List<Task> taskList = Arrays.asList(TestDataUtils.getTask(1l, 1), TestDataUtils.getTask(2l, 2));
		JasperReportData jasperReportData = reportData.getReportData(taskList);
		assertEquals(AppJasperReportType.TASKS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("tasks"));
		assertNotNull(jasperReportData.getParameters().get("tasks"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

	@Test
	public void whenGetReportDataFromListWithFilterReturnResult() {
		List<Task> taskList = Arrays.asList(TestDataUtils.getTask(1l, 1), TestDataUtils.getTask(2l, 2));
		JasperReportData jasperReportData = reportData.getReportData(taskList, null);
		assertEquals(AppJasperReportType.TASKS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("tasks"));
		assertNotNull(jasperReportData.getParameters().get("tasks"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

		jasperReportData = reportData.getReportData(taskList, new FilterData());
		assertEquals(AppJasperReportType.TASKS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("tasks"));
		assertNotNull(jasperReportData.getParameters().get("tasks"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

}