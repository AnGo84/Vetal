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
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.service.ClientServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {
	public static final String MAPPED_URL = "/clients";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ClientServiceImpl mockClientService;

	private Manager manager;
	private Client client;

    @BeforeEach
    public void beforeEach() {

		client = TestDataUtils.getClient(1l);
		manager = client.getManager();

		when(mockClientService.getAll()).thenReturn(Arrays.asList(client));
		when(mockClientService.get(anyLong())).thenReturn(client);
		when(mockClientService.findByCorpName(anyString())).thenReturn(client);
	}


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetClientListAsAuthorized_thenOk() throws Exception {
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
    public void whenGetClientListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowAddClientPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("contragentRecordPage"));
    }


    @Test
    public void whenShowAddClientPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditClientAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + client.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("readOnly", false))
				.andExpect(view().name("contragentRecordPage"));
    }

    @Test
    public void whenEditClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + client.getId()))
				//.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateClientAsAuthorizedWithNullClient_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("contragent", client)
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockClientService, times(1)).update(any());

	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateClientAsAuthorizedWithNullClientName_thenOk() throws Exception {
		when(mockClientService.isExist(any())).thenReturn(true);
		Client newClient = TestDataUtils.getClient(null);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("contragent", newClient)
		)
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attributeHasFieldErrors("contragent", "corpName"))
				.andExpect(view().name("contragentRecordPage"));

		verify(mockClientService, times(0)).update(client);
	}

    @Test
    public void whenUpdateClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeleteClientAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + client.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

        verify(mockClientService, times(1)).deleteById(client.getId());
	}

	@Test
	public void whenDeleteClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + client.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenViewTaskAsAuthorized_thenOk() throws Exception {

		mockMvc.perform(get(MAPPED_URL + "/view-" + client.getId()))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("contragent"))
				.andExpect(model().attribute("contragent", notNullValue()))
				.andExpect(model().attribute("contragent", hasProperty("id", notNullValue())))
				.andExpect(model().attribute("contragent", hasProperty("corpName", equalTo(client.getCorpName()))))
				.andExpect(model().attribute("readOnly", true))
				.andExpect(view().name("contragentRecordPage"));
	}

	@Test
	public void whenViewTaskAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/view-" + client.getId()))
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	/**/
	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		ContragentViewFilter contragentViewFilter = new ContragentViewFilter();
		contragentViewFilter.setCorpName("corpName");
		contragentViewFilter.setManager(manager);
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("contragentFilterData", contragentViewFilter.toString())
		)
				//.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
	}

    @Test
    public void whenFilterClientsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/filter"))
				//.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenClearFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenCleanFilterClientsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }


}