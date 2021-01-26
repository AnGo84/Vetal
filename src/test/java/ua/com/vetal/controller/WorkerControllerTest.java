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
import ua.com.vetal.entity.Worker;
import ua.com.vetal.service.WorkerServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkerControllerTest {
	public static final String MAPPED_URL = "/worker";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private WorkerServiceImpl mockWorkerService;

	private Worker worker;

	@BeforeEach
	public void beforeEach() {
		worker = TestDataUtils.getWorker(1l);
		when(mockWorkerService.getAll()).thenReturn(Arrays.asList(worker));
		when(mockWorkerService.get(anyLong())).thenReturn(worker);
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetWorkerListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("employeeList", notNullValue()))
				.andExpect(view().name("employeesPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("employeeList", notNullValue()))
				.andExpect(view().name("employeesPage"));
	}

	@Test
	public void whenGetWorkerListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddEmployeePageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("employeeRecordPage"));
	}


	@Test
	public void whenShowAddEmployeePageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditEmployeeAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + worker.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("employeeRecordPage"));
	}

	@Test
	public void whenEditEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + worker.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateEmployeeAsAuthorizedWithNullWorker_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("employee", hasProperty("id", nullValue())))
				.andExpect(model().attribute("employee", hasProperty("firstName", blankOrNullString())))
				.andExpect(model().attribute("employee", hasProperty("lastName", blankOrNullString())))
				.andExpect(model().attribute("employee", hasProperty("middleName", blankOrNullString())))
				.andExpect(model().attribute("employee", hasProperty("email", blankOrNullString())))
				.andExpect(view().name("employeeRecordPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateEmployeeAsAuthorizedWithNotNullWorker_thenOk() throws Exception {
		mockWorkerService.update(worker);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(worker.getId()))
				.param("firstName", worker.getFirstName())
				.param("lastName", worker.getLastName())
				.param("middleName", worker.getMiddleName())
				.param("email", worker.getEmail()))
				//.andDo(print())
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockWorkerService, times(1)).update(worker);
	}


	@Test
	public void whenUpdateEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteEmployeeAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + worker.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockWorkerService, times(1)).deleteById(worker.getId());
	}

	@Test
	public void whenDeleteEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + worker.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}
}