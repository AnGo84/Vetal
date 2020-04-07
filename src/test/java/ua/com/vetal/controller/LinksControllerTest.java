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
import ua.com.vetal.entity.Link;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.service.LinkServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LinksControllerTest {
    public static final String MAPPED_URL = "/links";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LinkServiceImpl mockLinkService;

    private LinkType linkType;
    private Link link;

    @BeforeEach
    public void beforeEach() {
        linkType = TestDataUtils.getLinkType(1l, "file");

        link = TestDataUtils.getLink(1l, "fullName", "shortName", linkType, "path");

        when(mockLinkService.findAllObjects()).thenReturn(Arrays.asList(link));
        when(mockLinkService.findById(anyLong())).thenReturn(link);
        when(mockLinkService.findByName(anyString())).thenReturn(link);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetLinkListAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("linksList", notNullValue()))
                .andExpect(view().name("linksPage"));

        mockMvc.perform(get(MAPPED_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("linksList", notNullValue()))
                .andExpect(view().name("linksPage"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetPersonListAsAuthorizedWithWrongRoleMANAGER_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenGetLinkListAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowAddLinkPageAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("link"))
                .andExpect(model().attribute("link", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("linkPage"));
    }


    @Test
    public void whenShowAddLinkPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditLinkAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + link.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("link"))
                .andExpect(model().attribute("link", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("linkPage"));
    }

    @Test
    public void whenEditLinkAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + link.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateLinkAsAuthorizedWithNullLink_thenOk() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("link"))
                .andExpect(model().attribute("link", notNullValue()))
                .andExpect(model().attribute("link", hasProperty("id", nullValue())))
                .andExpect(model().attribute("link", hasProperty("fullName", blankOrNullString())))
                .andExpect(model().attribute("link", hasProperty("shortName", blankOrNullString())))
                .andExpect(model().attribute("link", hasProperty("linkType", blankOrNullString())))
                .andExpect(model().attribute("link", hasProperty("path", blankOrNullString())))
                .andExpect(view().name("linkPage"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateLinkAsAuthorizedWithNotNullLink_thenOk() throws Exception {
        mockLinkService.updateObject(link);

        mockMvc.perform(post(MAPPED_URL + "/update")
                .param("id", String.valueOf(link.getId()))
                .param("fullName", link.getFullName())
                .param("shortName", link.getShortName())
                .param("linkType", String.valueOf(linkType.getId()))
                .param("path", link.getPath())
        )
                .andDo(print())
                .andExpect(status().isFound())

                .andExpect(redirectedUrl(MAPPED_URL));
        verify(mockLinkService, times(1)).updateObject(link);
    }


    @Test
    public void whenUpdateLinkAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeleteLinkAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + link.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));

        verify(mockLinkService, times(1)).deleteById(link.getId());
    }

    @Test
    public void whenDeleteLinkAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + link.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }


}