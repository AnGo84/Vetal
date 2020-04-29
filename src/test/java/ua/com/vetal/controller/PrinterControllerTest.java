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
        when(mockPrinterService.findAllObjects()).thenReturn(Arrays.asList(printer));
        when(mockPrinterService.findById(anyLong())).thenReturn(printer);
    }

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetPrinterListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("personsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("personsPage"));
	}

	@Test
	public void whenGetPrinterListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddPersonPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("personRecordPage"));
	}


	@Test
	public void whenShowAddPersonPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditPersonAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + printer.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("personRecordPage"));
	}

	@Test
	public void whenEditPersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdatePersonAsAuthorizedWithNullPrinter_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
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
	public void whenUpdatePersonAsAuthorizedWithNotNullPrinter_thenOk() throws Exception {
		//doNothing().when(mockUserService).updateObject(any(User.class));
		mockPrinterService.updateObject(printer);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(printer.getId()))
				.param("firstName", printer.getFirstName())
				.param("lastName", printer.getLastName())
				.param("middleName", printer.getMiddleName())
				.param("email", printer.getEmail()))
				//.andDo
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockPrinterService, times(1)).updateObject(printer);
	}


	@Test
	public void whenUpdatePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeletePersonAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockPrinterService, times(1)).deleteById(printer.getId());
	}

	@Test
	public void whenDeletePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + printer.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

}