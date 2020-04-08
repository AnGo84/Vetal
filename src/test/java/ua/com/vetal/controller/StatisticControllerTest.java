package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.service.OrderServiceImpl;
import ua.com.vetal.service.reports.ExporterService;
import ua.com.vetal.service.reports.JasperService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private ExporterService mockExporterService;
    @MockBean
    private JasperService jasperService;

    private List<Order> orders;

    @BeforeEach
    public void beforeEach() {
        orders = new ArrayList<>();

        orders.add(TestDataUtils.getOrder(1l));
        orders.add(TestDataUtils.getOrder(2l));
        when(mockOrderService.findAllObjects()).thenReturn(orders);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetOrderListAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("ordersList", notNullValue()))
                .andExpect(view().name("statisticPage"));
    }


    @Test
    public void whenGetClientListAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetCrossReportAsAuthorized_thenOk() throws Exception {
        //when(mockExporterService.export(any(ReportType.class), any(), anyString(), any()).thenReturn(null);
        mockMvc.perform(get(MAPPED_URL + "/crossReport"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void whenGetCrossReportAsNoAuthorized_thenOk() throws Exception {

        mockMvc.perform(get(MAPPED_URL + "/crossReport"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Disabled("Fix filters")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        FilterData filterData = new FilterData();
        filterData.setClient(orders.get(0).getClient());
        filterData.setManager(orders.get(0).getManager());
        mockMvc.perform(get(MAPPED_URL + "/filter")
                .param("statisticFilterData", filterData.toString())
        )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(model().attributeExists("statisticFilterData"))
                .andExpect(model().attribute("statisticFilterData", notNullValue()))
                .andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenClearFilterClientsAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/filter"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    @Disabled("Fix filters")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenClearFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenFilterClientsAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }
}