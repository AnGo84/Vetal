package ua.com.vetal.report.jasperReport.reportdata;

import net.sf.jasperreports.engine.JREmptyDataSource;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContractorJasperReportDataTest {

	private ContractorJasperReportData reportData = new ContractorJasperReportData();

	@Test
	public void whenGetReportDataFromTaskReturnResult() {
		Contractor contractor = TestDataUtils.getContractor(1l);
		JasperReportData jasperReportData = reportData.getReportData(contractor);
		assertNull(jasperReportData);
	}


	@Test
	public void whenGetReportDataFromListReturnResult() {
		List<Contractor> contractorList = Arrays.asList(TestDataUtils.getContractor(1l), TestDataUtils.getContractor(2l));
		JasperReportData jasperReportData = reportData.getReportData(contractorList);
		assertEquals(AppJasperReportType.CONTRACTORS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("contractors"));
		assertNotNull(jasperReportData.getParameters().get("contractors"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

	@Test
	public void whenGetReportDataFromListWithFilterReturnResult() {
		List<Contractor> contractorList = Arrays.asList(TestDataUtils.getContractor(1l), TestDataUtils.getContractor(2l));
		JasperReportData jasperReportData = reportData.getReportData(contractorList, null);
		assertEquals(AppJasperReportType.CONTRACTORS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("contractors"));
		assertNotNull(jasperReportData.getParameters().get("contractors"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

		jasperReportData = reportData.getReportData(contractorList, new PersonFilter());
		assertEquals(AppJasperReportType.CONTRACTORS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("contractors"));
		assertNotNull(jasperReportData.getParameters().get("contractors"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}
}