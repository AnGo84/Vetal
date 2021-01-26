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
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.PersonViewFilter;
import ua.com.vetal.service.ContractorServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContractorControllerTest {
	public static final String MAPPED_URL = "/contractor";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ContractorServiceImpl mockContractorService;

	private Manager manager;
	private Contractor contractor;

	@BeforeEach
	public void beforeEach() {
		contractor = TestDataUtils.getContractor(1l);
		manager = contractor.getManager();

		when(mockContractorService.findAllObjects()).thenReturn(Arrays.asList(contractor));
		when(mockContractorService.findById(anyLong())).thenReturn(contractor);
		when(mockContractorService.findByName(anyString())).thenReturn(contractor);
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetContractorListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("contractorsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("personList", notNullValue()))
				.andExpect(view().name("contractorsPage"));
	}

	@Test
	public void whenGetContractorListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenShowAddContractorPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("contractorRecordPage"));
	}


	@Test
	public void whenShowAddContractorPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditContractorAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + contractor.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attribute("person", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("contractorRecordPage"));
	}

	@Test
	public void whenEditContractorAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + contractor.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateContractorAsAuthorizedWithNullContractor_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("person", contractor)
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockContractorService, times(1)).updateObject(any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateContractorAsAuthorizedWithNotNullContractor_thenOk() throws Exception {
		Contractor newContractor = TestDataUtils.getContractor(null);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("person", newContractor)
		)
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("person"))
				.andExpect(model().attributeHasFieldErrors("person", "corpName"))
				.andExpect(view().name("contractorRecordPage"));

		verify(mockContractorService, times(0)).updateObject(contractor);
	}


	@Test
	public void whenUpdateContractorAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteContractorAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + contractor.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockContractorService, times(1)).deleteById(contractor.getId());
	}

	@Test
	public void whenDeleteContractorAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + contractor.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterContractorsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		PersonViewFilter personViewFilter = new PersonViewFilter();
		personViewFilter.setCorpName("corpName");
		personViewFilter.setManager(manager);
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("personFilterData", personViewFilter.toString())
		)
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenFilterContractorsAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/filter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenClearFilterContractorsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

	@Test
	public void whenCleanFilterContractorsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

}