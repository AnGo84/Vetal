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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {
    public static final String URL_PREFIX = "/manager";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ManagerServiceImpl mockManagerService;

    private Manager manager;

    @BeforeEach
    public void beforeEach() {
        manager = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");
        manager.setId(1l);

        when(mockManagerService.findAllObjects()).thenReturn(Arrays.asList(manager));
        when(mockManagerService.findById(anyLong())).thenReturn(manager);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetManagerListAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("personList", notNullValue()))
                .andExpect(view().name("personsPage"));

        mockMvc.perform(get(URL_PREFIX + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("personList", notNullValue()))
                .andExpect(view().name("personsPage"));
    }

    @Test
    public void whenGetManagerListAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowAddPersonPageAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("person"))
                .andExpect(model().attribute("person", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("personRecordPage"));
    }


    @Test
    public void whenShowAddPersonPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditPersonAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/edit-" + manager.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("person"))
                .andExpect(model().attribute("person", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("personRecordPage"));
    }

    @Test
    public void whenEditPersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/edit-" + manager.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdatePersonAsAuthorizedWithNullManager_thenOk() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/update"))
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
    public void whenUpdatePersonAsAuthorizedWithNotNullManager_thenOk() throws Exception {
        //doNothing().when(mockUserService).updateObject(any(User.class));
        mockManagerService.updateObject(manager);

        mockMvc.perform(post(URL_PREFIX + "/update")
                .param("id", String.valueOf(manager.getId()))
                .param("firstName", manager.getFirstName())
                .param("lastName", manager.getLastName())
                .param("middleName", manager.getMiddleName())
                .param("email", manager.getEmail()))
                .andDo(print())
                .andExpect(status().isFound())

                .andExpect(redirectedUrl(URL_PREFIX));
        verify(mockManagerService, times(1)).updateObject(manager);
    }


    @Test
    public void whenUpdatePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/update"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeletePersonAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/delete-" + manager.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL_PREFIX));

        verify(mockManagerService, times(1)).deleteById(manager.getId());
    }

    @Test
    public void whenDeletePersonAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/delete-" + manager.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

}