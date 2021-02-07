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
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.service.ContractorServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
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

		when(mockContractorService.getAll()).thenReturn(Arrays.asList(contractor));
		when(mockContractorService.get(anyLong())).thenReturn(contractor);
		when(mockContractorService.findByCorpName(anyString())).thenReturn(contractor);
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetContractorListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("contragentList", notNullValue()))
				.andExpect(view().name("contragentsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("contragentList", notNullValue()))
				.andExpect(view().name("contragentsPage"));
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
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("contragentRecordPage"));
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
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("contragentRecordPage"));
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
				.flashAttr("contragent", contractor)
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockContractorService, times(1)).update(any());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateContractorAsAuthorizedWithNotNullContractor_thenOk() throws Exception {
		when(mockContractorService.isExist(any())).thenReturn(true);
		Contractor newContractor = TestDataUtils.getContractor(null);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("contragent", newContractor)
		)
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attributeHasFieldErrors("contragent", "corpName"))
				.andExpect(view().name("contragentRecordPage"));

		verify(mockContractorService, times(0)).update(contractor);
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
	public void whenViewTaskAsAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/view-" + contractor.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("contragent", hasProperty("id", notNullValue())))
				.andExpect(model().attribute("contragent", hasProperty("corpName", equalTo(contractor.getCorpName()))))
				.andExpect(model().attribute("readOnly", true))
				.andExpect(view().name("contragentRecordPage"));
	}

	@Test
	public void whenViewTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/view-" + contractor.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	/**/
	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterContractorsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		ContragentViewFilter contragentViewFilter = new ContragentViewFilter();
		contragentViewFilter.setCorpName("corpName");
		contragentViewFilter.setManager(manager);
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("contragentFilterData", contragentViewFilter.toString())
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