package ua.com.vetal.report.jasperReport.reportdata;

import net.sf.jasperreports.engine.JREmptyDataSource;
import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.report.jasperReport.AppJasperReportType;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderJasperReportDataTest {

	private OrderJasperReportData reportData = new OrderJasperReportData();

	@Test
	public void whenGetReportDataFromTaskReturnResult() {
		Order order = TestDataUtils.getOrder(1l);
		JasperReportData jasperReportData = reportData.getReportData(order);
		assertNull(jasperReportData);
	}


	@Test
	public void whenGetReportDataFromListReturnResult() {
		List<Order> orderList = Arrays.asList(TestDataUtils.getOrder(1l), TestDataUtils.getOrder(2l));
		JasperReportData jasperReportData = reportData.getReportData(orderList);
		assertEquals(AppJasperReportType.ORDERS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(1, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("orders"));
		assertNotNull(jasperReportData.getParameters().get("orders"));
		assertNotNull(jasperReportData.getDataSource());
		assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
	}

	@Test
	public void whenGetReportDataFromListWithFilterReturnResult() {
		List<Order> orderList = Arrays.asList(TestDataUtils.getOrder(1l), TestDataUtils.getOrder(2l));
		JasperReportData jasperReportData = reportData.getReportData(orderList, null);
		assertEquals(AppJasperReportType.ORDERS_REPORT.getReportName(), jasperReportData.getReportName());
		assertNotNull(jasperReportData.getParameters());
		assertEquals(4, jasperReportData.getParameters().size());
		assertTrue(jasperReportData.getParameters().containsKey("orders"));
		assertNotNull(jasperReportData.getParameters().get("orders"));
		assertTrue(jasperReportData.getParameters().containsKey("filter"));
		assertNotNull(jasperReportData.getParameters().get("filter"));

        assertTrue(jasperReportData.getParameters().containsKey("date_From"));
        assertNotNull(jasperReportData.getParameters().get("date_From"));
        assertEquals("", jasperReportData.getParameters().get("date_From"));
        assertTrue(jasperReportData.getParameters().containsKey("date_Till"));
        assertNotNull(jasperReportData.getParameters().get("date_Till"));
        assertEquals("", jasperReportData.getParameters().get("date_Till"));
        assertNotNull(jasperReportData.getDataSource());
        assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

        jasperReportData = reportData.getReportData(orderList, new OrderViewFilter());
        assertEquals(AppJasperReportType.ORDERS_REPORT.getReportName(), jasperReportData.getReportName());
        assertNotNull(jasperReportData.getParameters());
        assertEquals(4, jasperReportData.getParameters().size());
        assertTrue(jasperReportData.getParameters().containsKey("orders"));
        assertNotNull(jasperReportData.getParameters().get("orders"));
        assertTrue(jasperReportData.getParameters().containsKey("filter"));
        assertNotNull(jasperReportData.getParameters().get("filter"));

        assertTrue(jasperReportData.getParameters().containsKey("date_From"));
        assertNotNull(jasperReportData.getParameters().get("date_From"));
        assertEquals("", jasperReportData.getParameters().get("date_From"));
        assertTrue(jasperReportData.getParameters().containsKey("date_Till"));
        assertNotNull(jasperReportData.getParameters().get("date_Till"));
        assertEquals("", jasperReportData.getParameters().get("date_Till"));
        assertNotNull(jasperReportData.getDataSource());
        assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);

        OrderViewFilter orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(new Date());
        orderViewFilter.setDateBeginTill(new Date());
        jasperReportData = reportData.getReportData(orderList, orderViewFilter);
        assertEquals(AppJasperReportType.ORDERS_REPORT.getReportName(), jasperReportData.getReportName());
        assertNotNull(jasperReportData.getParameters());
        assertEquals(4, jasperReportData.getParameters().size());
        assertTrue(jasperReportData.getParameters().containsKey("orders"));
        assertNotNull(jasperReportData.getParameters().get("orders"));
        assertTrue(jasperReportData.getParameters().containsKey("filter"));
        assertNotNull(jasperReportData.getParameters().get("filter"));
        assertTrue(jasperReportData.getParameters().containsKey("date_From"));
        assertNotNull(jasperReportData.getParameters().get("date_From"));
        assertEquals(DateUtils.SIMPLE_DATE_FORMAT.format(orderViewFilter.getDateBeginFrom()), jasperReportData.getParameters().get("date_From"));

        assertTrue(jasperReportData.getParameters().containsKey("date_Till"));
        assertNotNull(jasperReportData.getParameters().get("date_Till"));
        assertEquals(DateUtils.SIMPLE_DATE_FORMAT.format(orderViewFilter.getDateBeginTill()), jasperReportData.getParameters().get("date_Till"));

        assertNotNull(jasperReportData.getDataSource());

        assertTrue(jasperReportData.getDataSource().getClass() == JREmptyDataSource.class);
    }

}