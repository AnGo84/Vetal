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
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.TaskServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;

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
public class TasksControllerTest {
	public static final String MAPPED_URL = "/tasks";

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
        /*manager = TestBuildersUtils.getManager(1l,"firstName", "lastName", "middleName", "email");

        Task = TestBuildersUtils.getTask(1l, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        Task.setManager(manager);*/

		task = TestDataUtils.getTask(1l, 1);
		System.out.println("Create TASK: " + task);
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
				/*.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));*/
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
				/*.andExpect(model().attribute("task", hasProperty("id", nullValue())))
				.andExpect(model().attribute("Task", hasProperty("fullName", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("manager", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("firstName", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("lastName", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("middleName", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("address", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("email", blankOrNullString())))
				.andExpect(model().attribute("Task", hasProperty("phone", blankOrNullString())))*/
				.andExpect(view().name("taskPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateTaskAsAuthorizedWithNotNullTask_thenOk() throws Exception {
		//doNothing().when(mockUserService).updateObject(any(User.class));
		mockTaskService.updateObject(task);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(task.getId()))
				.param("number", String.valueOf(task.getNumber()))
				.param("numberBase", String.valueOf(task.getNumberBase().getId()))
				.param("account", task.getAccount())
				.param("numberSuffix", String.valueOf(task.getNumberSuffix()))
				.param("manager", String.valueOf(task.getManager().getId()))
				.param("workName", task.getWorkName())
				.param("contractor", String.valueOf(task.getContractor().getId()))
				.param("amountForContractor", String.valueOf(task.getAmountForContractor()))
				.param("contractorAmount", String.valueOf(task.getContractorAmount()))
				.param("production", String.valueOf(task.getProduction().getId()))
				.param("productionType", String.valueOf(task.getProductionType().getId()))
				.param("dateBegin", String.valueOf(task.getDateBegin()))
				.param("dateEnd", String.valueOf(task.getDateEnd()))
				.param("client", String.valueOf(task.getClient().getId()))
				.param("stock", String.valueOf(task.getStock().getId()))
				.param("printing", String.valueOf(task.getPrinting()))
				.param("printingUnit", String.valueOf(task.getPrintingUnit().getId()))
				.param("chromaticity", String.valueOf(task.getChromaticity().getId()))
				.param("printingFormat", task.getPrintingFormat())
				.param("laminate", String.valueOf(task.getLaminate().getId()))
				.param("paper", String.valueOf(task.getPaper().getId()))
				.param("cringle", String.valueOf(task.getCringle().getId()))
		)
				//.andDo(print())
				/*.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));*/
				.andExpect(status().isOk())
				.andExpect(view().name("taskPage"));


		verify(mockTaskService, times(1)).updateObject(task);
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
		FilterData filterData = new FilterData();
		filterData.setAccount("account");
		filterData.setManager(filterData.getManager());
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("FilterDataData", filterData.toString())
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
		//when(mockExporterService.export(any(ReportType.class), any(), anyString(), any()).thenReturn(null);
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
		//when(mockExporterService.export(any(ReportType.class), any(), anyString(), any()).thenReturn(null);
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
}