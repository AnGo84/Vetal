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
import ua.com.vetal.entity.Manager;
import ua.com.vetal.service.ManagerServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {
    public static final String MAPPED_URL = "/manager";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ManagerServiceImpl mockManagerService;

    private Manager manager;

    @BeforeEach
    public void beforeEach() {
		manager = TestDataUtils.getManager(1l);

		when(mockManagerService.getAll()).thenReturn(Arrays.asList(manager));
		when(mockManagerService.get(anyLong())).thenReturn(manager);
	}

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetManagerListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("employeeList", notNullValue()))
				.andExpect(view().name("employeesPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("employeeList", notNullValue()))
				.andExpect(view().name("employeesPage"));
    }

    @Test
    public void whenGetManagerListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
                .andExpect(status().isOk());
    }

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddEmployeePageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("employeeRecordPage"));
	}


	@Test
	public void whenShowAddEmployeePageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditEmployeeAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + manager.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("employeeRecordPage"));
	}

	@Test
	public void whenEditEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + manager.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateEmployeeAsAuthorizedWithNullManager_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
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
	public void whenUpdateEmployeeAsAuthorizedWithNotNullManager_thenOk() throws Exception {
		mockManagerService.update(manager);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(manager.getId()))
				.param("firstName", manager.getFirstName())
				.param("lastName", manager.getLastName())
				.param("middleName", manager.getMiddleName())
				.param("email", manager.getEmail()))
				//.andDo
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockManagerService, times(1)).update(manager);
	}

	@Test
	public void whenUpdateEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteEmployeeAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + manager.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockManagerService, times(1)).deleteById(manager.getId());
	}

	@Test
	public void whenDeleteEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + manager.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

}