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
import ua.com.vetal.entity.filter.ClientViewFilter;
import ua.com.vetal.service.ClientServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
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

        when(mockClientService.findAllObjects()).thenReturn(Arrays.asList(client));
        when(mockClientService.findById(anyLong())).thenReturn(client);
        when(mockClientService.findByName(anyString())).thenReturn(client);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetClientListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("clientsList", notNullValue()))
                .andExpect(view().name("clientsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("clientsList", notNullValue()))
                .andExpect(view().name("clientsPage"));
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
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("clientPage"));
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
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("clientPage"));
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
				.flashAttr("client", client)
		)
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockClientService, times(1)).updateObject(any());

	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateClientAsAuthorizedWithNullClientName_thenOk() throws Exception {
		when(mockClientService.isFullNameExist(any())).thenReturn(true);
		Client newClient = TestDataUtils.getClient(null);
		mockMvc.perform(post(MAPPED_URL + "/update")
				.flashAttr("client", newClient)
		)
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("client"))
				.andExpect(model().attributeHasFieldErrors("client", "fullName"))
				.andExpect(view().name("clientPage"));

		verify(mockClientService, times(0)).updateObject(client);
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
    public void whenFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        ClientViewFilter clientViewFilter = new ClientViewFilter();
        clientViewFilter.setFullName("fullName");
        clientViewFilter.setManager(manager);
        mockMvc.perform(get(MAPPED_URL + "/filter")
                .param("clientFilterData", clientViewFilter.toString())
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenFilterClientsAsNoAuthorized_thenRedirectToMappedURL() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/filter"))
                //.andDo
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