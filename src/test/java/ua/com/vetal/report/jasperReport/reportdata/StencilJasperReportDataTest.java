package ua.com.vetal.report.jasperReport.reportdata;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StencilJasperReportDataTest {

	private StencilJasperReportData reportData = new StencilJasperReportData();

	@Test
	public void whenGetReportDataFromTaskReturnResult() {
		Stencil stencil = TestDataUtils.getStencil(1l, 1);
		JasperReportData jasperReportData = reportData.getReportData(stencil);
		assertEquals(AppJasperReportType.STENCIL_REPORT.getReportName(), jasperReportData.getReportName());
		assertNull(jasperReportData.getParameters());
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JRBeanCollectionDataSource.class);
	}


	@Test
	public void whenGetReportDataFromListReturnResult() {
		List<Stencil> stencilList = Arrays.asList(TestDataUtils.getStencil(1l, 1), TestDataUtils.getStencil(2l, 2));
		JasperReportData jasperReportData = reportData.getReportData(stencilList);
		assertEquals(AppJasperReportType.STENCILS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("stencils"));
		assertNotNull(jasperReportData.getParameters().get("stencils"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

	@Test
	public void whenGetReportDataFromListWithFilterReturnResult() {
		List<Stencil> stencilList = Arrays.asList(TestDataUtils.getStencil(1l, 1), TestDataUtils.getStencil(2l, 2));
		JasperReportData jasperReportData = reportData.getReportData(stencilList, null);
		assertEquals(AppJasperReportType.STENCILS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("stencils"));
		assertNotNull(jasperReportData.getParameters().get("stencils"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

		jasperReportData = reportData.getReportData(stencilList, new FilterData());
		assertEquals(AppJasperReportType.STENCILS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("stencils"));
		assertNotNull(jasperReportData.getParameters().get("stencils"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}
}