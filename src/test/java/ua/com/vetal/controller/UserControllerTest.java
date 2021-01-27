package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String MAPPED_URL = "/users";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl mockUserService;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = TestBuildersUtils.getUser(1l, "New Name", "", true, null);
        user.setUserRoles(new HashSet<>(Arrays.asList(TestBuildersUtils.getUserRole(1l, "ROLE_ADMIN"))));

        when(mockUserService.findAllObjects()).thenReturn(Arrays.asList(user));
        when(mockUserService.findById(anyLong())).thenReturn(user);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetPersonListAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                ////.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", notNullValue()))
                .andExpect(view().name("usersPage"));
        mockMvc.perform(get(MAPPED_URL + "/all"))
                ////.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", notNullValue()))
                .andExpect(view().name("usersPage"));
        mockMvc.perform(get(MAPPED_URL + "/list"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", notNullValue()))
                .andExpect(view().name("usersPage"));
    }

    @Test
    public void whenGetPersonListAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                ////.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetPersonListAsAuthorizedWithWrongRoleMANAGER_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                ////.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetShowAddUserPageAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                ////.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("userPage"));
    }

    @Test
    public void whenGetShowAddUserPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                ////.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditUserAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + user.getId()))
                ////.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("userPage"));
    }

    @Test
    public void whenEditUserAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + user.getId()))
                ////.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateUserAsAuthorizedWithNullUser_thenOk() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                ////.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("user", hasProperty("id", nullValue())))
                .andExpect(model().attribute("user", hasProperty("name", blankOrNullString())))
                .andExpect(model().attribute("user", hasProperty("enabled", equalTo(false))))
                .andExpect(model().attribute("user", hasProperty("userRoles", empty())))
                .andExpect(model().attributeHasFieldErrors("user", "userRoles", "name"))
                .andExpect(view().name("userPage"));

        verify(mockUserService, times(0)).updateObject(any());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateUserAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update")
                .flashAttr("user", user)
        )
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));

        verify(mockUserService, times(1)).updateObject(user);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateUserAsAuthorizedErrorOnSave() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(mockUserService).updateObject(any(User.class));

        mockMvc.perform(post(MAPPED_URL + "/update")
                .param("id", String.valueOf(user.getId()))
                .param("name", user.getName())
                .param("encryptedPassword", "password")
                .param("enabled", String.valueOf(user.isEnabled()))
                .param("userRoles", "1")
        )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "name"))
                .andExpect(view().name("userPage"));

        verify(mockUserService, times(0)).updateObject(user);
    }

    @Test
    public void whenUpdateUserAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeleteUserAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));

        verify(mockUserService, times(1)).deleteById(user.getId());
    }

    @Test
    public void whenDeleteUserAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenResetUserPasswordAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        when(mockUserService.isObjectExist(any())).thenReturn(true);
        mockMvc.perform(get(MAPPED_URL + "/resetPassword-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/edit-" + user.getId() + "?resetSuccess"));
        when(mockUserService.isObjectExist(any())).thenReturn(false);

        User notExistUser = TestBuildersUtils.getUser(123654321l, "New Name", "", true, null);
        when(mockUserService.findById(anyLong())).thenReturn(notExistUser);
        mockMvc.perform(get(MAPPED_URL + "/resetPassword-" + notExistUser.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/edit-" + notExistUser.getId()));
    }

    @Test
    public void whenResetUserPasswordAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/resetPassword-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

}