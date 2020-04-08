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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
		//Worker = TestBuildersUtils.getWorker(1l, "firstName", "lastName", "middleName", "email");
		worker = TestDataUtils.getWorker(1l);
		when(mockWorkerService.findAllObjects()).thenReturn(Arrays.asList(worker));
		when(mockWorkerService.findById(anyLong())).thenReturn(worker);
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetWorkerListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("personsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("personsPage"));
	}

	@Test
	public void whenGetWorkerListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddPersonPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("personRecordPage"));
	}


	@Test
	public void whenShowAddPersonPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditPersonAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + worker.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("personRecordPage"));
	}

	@Test
	public void whenEditPersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + worker.getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdatePersonAsAuthorizedWithNullWorker_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("person", hasProperty("id", nullValue())))
				.andExpect(model().attribute("person", hasProperty("firstName", blankOrNullString())))
				.andExpect(model().attribute("person", hasProperty("lastName", blankOrNullString())))
				.andExpect(model().attribute("person", hasProperty("middleName", blankOrNullString())))
				.andExpect(model().attribute("person", hasProperty("email", blankOrNullString())))
				.andExpect(view().name("personRecordPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdatePersonAsAuthorizedWithNotNullWorker_thenOk() throws Exception {
		//doNothing().when(mockUserService).updateObject(any(User.class));
		mockWorkerService.updateObject(worker);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(worker.getId()))
				.param("firstName", worker.getFirstName())
				.param("lastName", worker.getLastName())
				.param("middleName", worker.getMiddleName())
				.param("email", worker.getEmail()))
				.andDo(print())
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockWorkerService, times(1)).updateObject(worker);
	}


	@Test
	public void whenUpdatePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeletePersonAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + worker.getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockWorkerService, times(1)).deleteById(worker.getId());
	}

	@Test
	public void whenDeletePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + worker.getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}
}