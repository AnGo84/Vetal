package ua.com.vetal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.TaskServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.StringUtils;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
	public static final String MAPPED_URL = "/tasks";
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TaskServiceImpl mockTaskService;
	@MockBean
	private TaskJasperReportData reportData;
	@MockBean
	private JasperReportService mockJasperReportService;
	@MockBean
	private MailServiceImp mailServiceImp;
	private Task task;

	@BeforeEach
	public void beforeEach() {
		task = TestDataUtils.getTask(1l, 1);
		when(mockTaskService.findAllObjects()).thenReturn(Arrays.asList(task));
		when(mockTaskService.findById(anyLong())).thenReturn(task);
		when(mockTaskService.findByAccount(anyString())).thenReturn(task);
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetTaskListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("title", "Tasks"))
				.andExpect(model().attribute("tasksList", notNullValue()))
				.andExpect(view().name("tasksPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo(print())
				.andExpect(model().attribute("title", "Tasks"))
				.andExpect(model().attribute("tasksList", notNullValue()))
				.andExpect(view().name("tasksPage"));
	}

	@Test
	public void whenGetTaskListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddTaskPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task"))
				.andExpect(model().attribute("task", notNullValue()))
				//.andExpect(model().attribute("edit", false))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("taskPage"));
	}


	@Test
	public void whenShowAddTaskPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditTaskAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task"))
				.andExpect(model().attribute("task", notNullValue()))
				//.andExpect(model().attribute("edit", true))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("taskPage"));
	}

	@Test
	public void whenEditTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenCopyTaskAsAuthorized_thenOk() throws Exception {
		when(mockTaskService.getMaxID()).thenReturn(100l);
		mockMvc.perform(get(MAPPED_URL + "/copy-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task"))
				.andExpect(model().attribute("task", notNullValue()))
				.andExpect(model().attribute("task", hasProperty("id", nullValue())))
				.andExpect(model().attribute("task", hasProperty("number", notNullValue())))
				.andExpect(model().attribute("task", hasProperty("number", is(101))))
				.andExpect(model().attribute("task", hasProperty("account", nullValue())))
				.andExpect(model().attribute("task", hasProperty("fullNumber", nullValue())))
				.andExpect(model().attribute("task", hasProperty("dbFile", nullValue())))
				.andExpect(model().attribute("task", hasProperty("contractor", notNullValue())))
				.andExpect(model().attribute("task", hasProperty("client", notNullValue())))
				.andExpect(model().attribute("task", hasProperty("production", notNullValue())))
				.andExpect(model().attribute("task", hasProperty("productionType", notNullValue())))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("taskPage"));
	}

	@Test
	public void whenCopyTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/copy-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenViewTaskAsAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/view-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task"))
				.andExpect(model().attribute("task", notNullValue()))
				.andExpect(model().attribute("task", hasProperty("id", notNullValue())))
				.andExpect(model().attribute("task", hasProperty("account", equalTo(task.getAccount()))))
				//.andExpect(model().attribute("edit", true))
				.andExpect(model().attribute("readOnly", true))
				.andExpect(view().name("taskPage"));
	}

	@Test
	public void whenViewTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/view-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("taskPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateTaskAsAuthorizedWithNullTask_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task"))
				.andExpect(model().attribute("task", notNullValue()))
				.andExpect(view().name("taskPage"));

		verify(mockTaskService, times(0)).updateObject(any());
		verify(mockTaskService, times(0)).updateObject(any(), any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateTaskAsAuthorizedWithNotNullTask_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("task", task)
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockTaskService, times(0)).updateObject(any());
		verify(mockTaskService, times(1)).updateObject(any(), any());
	}


	@Test
	public void whenUpdateTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteTaskAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockTaskService, times(1)).deleteById(task.getId());
	}

	@Test
	public void whenDeleteTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterTasksAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		OrderViewFilter orderViewFilter = new OrderViewFilter();
		orderViewFilter.setAccount("account");
		orderViewFilter.setManager(orderViewFilter.getManager());
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("FilterDataData", orderViewFilter.toString())
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenFilterTasksAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/filter"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenClearFilterTasksAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenCleanFilterTasksAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenExportToExcelReportTaskAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/excelExport"))
				//.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	public void whenExportToExcelReportTaskAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/excelExport"))
				//.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenPdfReportTaskAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/pdfReport-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	public void whenPdfReportTaskAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/pdfReport-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenSendEmailAsAuthorized_thenOk() throws Exception {
		when(mockTaskService.taskMailingDeclineReason(any())).thenReturn("");
		mockMvc.perform(get(MAPPED_URL + "/sendEmail-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("title", "email"))
				.andExpect(model().attribute("resultSuccess", notNullValue()))
				.andExpect(model().attribute("resultSuccess", is(true)))
				.andExpect(model().attribute("message", notNullValue()))
				.andExpect(view().name("emailResultPage"));
	}

	@Test
	public void whenSendEmailAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/sendEmail-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenSendEmailAsAuthorized_thenError() throws Exception {
		when(mockTaskService.findById(anyLong())).thenReturn(null);
		mockMvc.perform(get(MAPPED_URL + "/sendEmail-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("title", "email"))
				.andExpect(model().attribute("resultSuccess", notNullValue()))
				.andExpect(model().attribute("resultSuccess", is(false)))
				.andExpect(model().attribute("message", notNullValue()))
				.andExpect(view().name("emailResultPage"));

		when(mockTaskService.findById(anyLong())).thenReturn(task);
		when(mockTaskService.taskMailingDeclineReason(any())).thenReturn("error message");
		mockMvc.perform(get(MAPPED_URL + "/sendEmail-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("title", "email"))
				.andExpect(model().attribute("resultSuccess", notNullValue()))
				.andExpect(model().attribute("resultSuccess", is(false)))
				.andExpect(view().name("emailResultPage"));
	}

	@Test
	public void whenDownloadFile_thenReturnResult() throws Exception {
		task.getDbFile().setFileType(MediaType.APPLICATION_JSON.toString());
		when(mockTaskService.findById(anyLong())).thenReturn(task);

		MvcResult mvcResult = mockMvc.perform(get(MAPPED_URL + "/downloadFile-" + task.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String headerValue = mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION);
		assertTrue(headerValue.contains(task.getDbFile().getFileName()));

		String bodyValue = mvcResult.getResponse().getContentAsString();
		assertFalse(StringUtils.isEmpty(bodyValue));
	}

	@Test
	public void whenDownloadFile_thenReturnEmptyResult() throws Exception {
		when(mockTaskService.findById(anyLong())).thenReturn(null);

		MvcResult mvcResult = mockMvc.perform(get(MAPPED_URL + "/downloadFile-" + task.getId()))
				.andDo(print())
				.andExpect(status().isForbidden())
				.andReturn();
		String bodyValue = mvcResult.getResponse().getContentAsString();
		assertFalse(StringUtils.isEmpty(bodyValue));
		assertTrue(bodyValue.contains("error"));
	}
}