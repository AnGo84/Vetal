package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.StatisticOrder;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.OrderServiceImpl;
import ua.com.vetal.service.reports.JasperReportService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticControllerTest {
	public static final String MAPPED_URL = "/statistic";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private OrderServiceImpl mockOrderService;

	@MockBean
	private TaskJasperReportData reportData;
	@MockBean
	private JasperReportService mockJasperReportService;

	private List<StatisticOrder> statisticOrders;

	@BeforeEach
	public void beforeEach() {
		statisticOrders = new ArrayList<>();

		statisticOrders.add(TestDataUtils.getOrder(1l));
		statisticOrders.add(TestDataUtils.getOrder(2l));
		when(mockOrderService.findAllObjects()).thenReturn(statisticOrders);
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetOrderListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("ordersList", notNullValue()))
				.andExpect(view().name("statisticPage"));
	}


	@Test
	public void whenGetClientListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetCrossReportAsAuthorized_thenOk() throws Exception {
		//when(mockExporterService.export(any(ReportType.class), any(), anyString(), any()).thenReturn(null);
		mockMvc.perform(get(MAPPED_URL + "/crossReport"))
				//.andDo
				.andExpect(status().isOk());

	}

	@Test
	public void whenGetCrossReportAsNoAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/crossReport"))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		OrderViewFilter orderViewFilter = new OrderViewFilter();
		orderViewFilter.setClient(statisticOrders.get(0).getClient());
		orderViewFilter.setManager(statisticOrders.get(0).getManager());
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("statisticFilterData", orderViewFilter.toString())
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenFilterClientsAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/filter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenClearFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenClearFilterClientsAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}
}