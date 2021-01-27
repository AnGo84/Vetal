package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.StateServiceImpl;
import ua.com.vetal.service.StencilServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;

import javax.mail.MessagingException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StencilsControllerTest {
	public static final String MAPPED_URL = "/stencils";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private StencilServiceImpl mockStencilService;

	@MockBean
	private TaskJasperReportData reportData;
	@MockBean
	private JasperReportService mockJasperReportService;
	@MockBean
	private StateServiceImpl mockStateService;
	@MockBean
	private MailServiceImp mockMailService;

	private Stencil stencil;

	@BeforeEach
	public void beforeEach() {
		stencil = TestDataUtils.getStencil(1l, 1);

		when(mockStencilService.findAllObjects()).thenReturn(Arrays.asList(stencil));
		when(mockStencilService.findById(anyLong())).thenReturn(stencil);
		when(mockStencilService.findByAccount(anyString())).thenReturn(stencil);

	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetStencilListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("title", "Stencils"))
				.andExpect(model().attribute("stencilsList", notNullValue()))
				.andExpect(view().name("stencilsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(model().attribute("title", "Stencils"))
				.andExpect(model().attribute("stencilsList", notNullValue()))
				.andExpect(view().name("stencilsPage"));
	}

	@Test
	public void whenGetStencilListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddStencilPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stencil"))
				.andExpect(model().attribute("stencil", notNullValue()))
				//.andExpect(model().attribute("edit", false))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("stencilPage"));
	}


	@Test
	public void whenShowAddStencilPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditStencilAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stencil"))
				.andExpect(model().attribute("stencil", notNullValue()))
				//.andExpect(model().attribute("edit", true))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("stencilPage"));
	}

	@Test
	public void whenEditStencilAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenCopyStencilAsAuthorized_thenOk() throws Exception {
		when(mockStencilService.getMaxID()).thenReturn(100l);
		mockMvc.perform(get(MAPPED_URL + "/copy-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stencil"))
				.andExpect(model().attribute("stencil", notNullValue()))
				.andExpect(model().attribute("stencil", hasProperty("id", nullValue())))
				.andExpect(model().attribute("stencil", hasProperty("number", notNullValue())))
				.andExpect(model().attribute("stencil", hasProperty("number", is(101))))
				.andExpect(model().attribute("stencil", hasProperty("account", nullValue())))
				.andExpect(model().attribute("stencil", hasProperty("fullNumber", nullValue())))
				.andExpect(model().attribute("stencil", hasProperty("manager", notNullValue())))
				.andExpect(model().attribute("stencil", hasProperty("client", notNullValue())))
				.andExpect(model().attribute("stencil", hasProperty("production", notNullValue())))
				.andExpect(model().attribute("stencil", hasProperty("paper", notNullValue())))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("stencilPage"));
	}

	@Test
	public void whenCopyStencilAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/copy-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenViewStencilAsAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/view-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stencil"))
				.andExpect(model().attribute("stencil", notNullValue()))
				.andExpect(model().attribute("stencil", hasProperty("id", notNullValue())))
				.andExpect(model().attribute("stencil", hasProperty("account", equalTo(stencil.getAccount()))))
				//.andExpect(model().attribute("edit", true))
				.andExpect(model().attribute("readOnly", true))
				.andExpect(view().name("stencilPage"));
	}

	@Test
	public void whenViewStencilAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/view-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(view().name("stencilPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateStencilAsAuthorizedWithNullStencil_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("stencil"))
				.andExpect(model().attribute("stencil", notNullValue()))
				.andExpect(view().name("stencilPage"));

		verify(mockMailService, times(0)).sendEmail(any());
		verify(mockStencilService, times(0)).updateObject(any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateStencilAsAuthorizedWithNotNullStencil_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("stencil", stencil)
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockMailService, times(0)).sendEmail(any());
		verify(mockStencilService, times(1)).updateObject(any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateStencilAsAuthorizedWithNotNullStencil_thenOkAndSendEmail() throws Exception {
		Stencil dbStencil = TestDataUtils.getStencil(1l, 2);
		when(mockStencilService.findById(any())).thenReturn(dbStencil);

		stencil.setId(1l);
		stencil.getState().setId(2l);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("stencil", stencil)
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockStencilService, times(1)).findById(stencil.getId());
		verify(mockMailService, times(1)).sendEmail(any());
		verify(mockStencilService, times(1)).updateObject(any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateStencilAsAuthorizedWithNotNullStencil_thenOkAndErrorEmail() throws Exception {
		Stencil dbStencil = TestDataUtils.getStencil(1l, 2);
		when(mockStencilService.findById(any())).thenReturn(dbStencil);
		doThrow(MessagingException.class).when(mockMailService).sendEmail(any());

		stencil.setId(1l);
		stencil.getState().setId(2l);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("stencil", stencil)
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockStencilService, times(1)).findById(stencil.getId());
		verify(mockMailService, times(1)).sendEmail(any());
		verify(mockStencilService, times(1)).updateObject(any());
	}


	@Test
	public void whenUpdateStencilAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteStencilAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockStencilService, times(1)).deleteById(stencil.getId());
	}

	@Test
	public void whenDeleteStencilAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterStencilsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		OrderViewFilter orderViewFilter = new OrderViewFilter();
		orderViewFilter.setAccount("account");
		orderViewFilter.setManager(orderViewFilter.getManager());
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("FilterDataData", orderViewFilter.toString())
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenFilterStencilsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/filter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenClearFilterStencilsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenClearFilterStencilsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenExportToExcelReportStencilAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/excelExport"))
				//.andDo
				.andExpect(status().isOk());

	}

	@Test
	public void whenExportToExcelReportStencilAsNoAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/excelExport"))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenPdfReportStencilAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/pdfReport-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk());

	}

	@Test
	public void whenPdfReportStencilAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/pdfReport-" + stencil.getId()))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	public void whenMakeReadyStencil_thenUpdateAndSendEmail() throws Exception {
		when(mockStateService.findById(anyLong())).thenReturn(TestBuildersUtils.getState(4l, "name task", "altname"));

		stencil.getState().setId(2l);
		mockMvc.perform(get(MAPPED_URL + "/make_ready-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockStateService, times(1)).findById(4l);
		verify(mockStencilService, times(1)).updateObject(stencil);
		verify(mockMailService, times(1)).sendEmail(any());

	}

	@Test
	public void whenMakeReadyStencil_thenUpdateNothing() throws Exception {
		stencil.getState().setId(1l);
		mockMvc.perform(get(MAPPED_URL + "/make_ready-" + stencil.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockStateService, times(0)).findById(4l);
		verify(mockStencilService, times(0)).updateObject(stencil);
		verify(mockMailService, times(0)).sendEmail(any());

	}

}