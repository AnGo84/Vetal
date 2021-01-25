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
import ua.com.vetal.entity.Printer;
import ua.com.vetal.service.PrinterServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PrinterControllerTest {
	public static final String MAPPED_URL = "/printer";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PrinterServiceImpl mockPrinterService;

	private Printer printer;

	@BeforeEach
	public void beforeEach() {
		//printer = TestBuildersUtils.getPrinter(1l, "firstName", "lastName", "middleName", "email");
		printer = TestDataUtils.getPrinter(1l);
		when(mockPrinterService.getAll()).thenReturn(Arrays.asList(printer));
		when(mockPrinterService.get(anyLong())).thenReturn(printer);
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetPrinterListAsAuthorized_thenOk() throws Exception {
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
	public void whenGetPrinterListAsNoAuthorized_thenOk() throws Exception {
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
		mockMvc.perform(get(MAPPED_URL + "/edit-" + printer.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("employeeRecordPage"));
	}

	@Test
	public void whenEditEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateEmployeeAsAuthorizedWithNullPrinter_thenOk() throws Exception {
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
	public void whenUpdateEmployeeAsAuthorizedWithNotNullPrinter_thenOk() throws Exception {
		//doNothing().when(mockUserService).updateObject(any(User.class));
		mockPrinterService.update(printer);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(printer.getId()))
				.param("firstName", printer.getFirstName())
				.param("lastName", printer.getLastName())
				.param("middleName", printer.getMiddleName())
				.param("email", printer.getEmail()))
				//.andDo
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockPrinterService, times(1)).update(printer);
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
		mockMvc.perform(get(MAPPED_URL + "/delete-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockPrinterService, times(1)).deleteById(printer.getId());
	}

	@Test
	public void whenDeleteEmployeeAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

}