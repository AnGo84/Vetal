package ua.com.vetal.report.jasperReport.reportdata;

import net.sf.jasperreports.engine.JREmptyDataSource;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientJasperReportDataTest {

	private ClientJasperReportData reportData = new ClientJasperReportData();

	@Test
	public void whenGetReportDataFromTaskReturnResult() {
		Client client = TestDataUtils.getClient(1l);
		JasperReportData jasperReportData = reportData.getReportData(client);
		assertNull(jasperReportData);
	}


	@Test
	public void whenGetReportDataFromListReturnResult() {
		List<Client> clientList = Arrays.asList(TestDataUtils.getClient(1l), TestDataUtils.getClient(2l));
		JasperReportData jasperReportData = reportData.getReportData(clientList);
		assertEquals(AppJasperReportType.CLIENTS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("clients"));
		assertNotNull(jasperReportData.getParameters().get("clients"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

	@Test
	public void whenGetReportDataFromListWithFilterReturnResult() {
		List<Client> clientList = Arrays.asList(TestDataUtils.getClient(1l), TestDataUtils.getClient(2l));
		JasperReportData jasperReportData = reportData.getReportData(clientList, null);
		assertEquals(AppJasperReportType.CLIENTS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("clients"));
		assertNotNull(jasperReportData.getParameters().get("clients"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

		jasperReportData = reportData.getReportData(clientList, new ClientFilter());
		assertEquals(AppJasperReportType.CLIENTS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("clients"));
		assertNotNull(jasperReportData.getParameters().get("clients"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}
}