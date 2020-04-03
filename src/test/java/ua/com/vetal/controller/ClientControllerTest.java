package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.service.ClientServiceImpl;

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
class ClientControllerTest {
    public static final String MAPPED_URL = "/clients";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientServiceImpl mockClientService;

    private Manager manager;
    private Client client;

    @BeforeEach
    public void beforeEach() {
        manager = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");
        manager.setId(1l);

        client = TestDataUtils.getClient(1l, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        client.setManager(manager);

        when(mockClientService.findAllObjects()).thenReturn(Arrays.asList(client));
        when(mockClientService.findById(anyLong())).thenReturn(client);
        when(mockClientService.findByName(anyString())).thenReturn(client);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetClientListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("clientsList", notNullValue()))
                .andExpect(view().name("clientsPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("clientsList", notNullValue()))
                .andExpect(view().name("clientsPage"));
    }

    @Test
    public void whenGetClientListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowAddClientPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("clientPage"));
    }


    @Test
    public void whenShowAddClientPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditClientAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + client.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("clientPage"));
    }

    @Test
    public void whenEditClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + client.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateClientAsAuthorizedWithNullClient_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", notNullValue()))
                .andExpect(model().attribute("client", hasProperty("id", nullValue())))
                .andExpect(model().attribute("client", hasProperty("fullName", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("manager", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("firstName", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("lastName", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("middleName", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("address", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("email", blankOrNullString())))
                .andExpect(model().attribute("client", hasProperty("phone", blankOrNullString())))
                .andExpect(view().name("clientPage"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateClientAsAuthorizedWithNotNullClient_thenOk() throws Exception {
        //doNothing().when(mockUserService).updateObject(any(User.class));
        mockClientService.updateObject(client);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(client.getId()))
				.param("fullName", client.getFullName())
				.param("manager", String.valueOf(client.getId()))
				.param("firstName", client.getFirstName())
				.param("lastName", client.getLastName())
				.param("middleName", client.getMiddleName())
				.param("address", client.getAddress())
				.param("email", client.getEmail())
				.param("phone", client.getPhone())
		)
				.andDo(print())
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockClientService, times(1)).updateObject(client);
    }


    @Test
    public void whenUpdateClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeleteClientAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + client.getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

        verify(mockClientService, times(1)).deleteById(client.getId());
    }

    @Test
    public void whenDeleteClientAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + client.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @Disabled("Fix filters")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        ClientFilter clientFilter = new ClientFilter();
        clientFilter.setFullName("fullName");
		clientFilter.setManager(manager);
		mockMvc.perform(get(MAPPED_URL + "/filter")
				.param("clientFilterData", clientFilter.toString())
		)
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(model().attributeExists("clientFilterData"))
				.andExpect(model().attribute("clientFilterData", notNullValue()))
				.andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenClearFilterClientsAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/filter"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @Disabled("Fix filters")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenClearFilterClientsAsAuthorizedWithNotNullUser_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));
    }

    @Test
    public void whenFilterClientsAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/clearFilter"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }


}