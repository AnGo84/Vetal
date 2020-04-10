package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserViewControllerTest {
    public static final String MAPPED_URL = "/user";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl mockUserService;
    @MockBean
    private PasswordResetTokenRepository mockTokenRepository;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = TestBuildersUtils.getUser(1l, "New Name", "", true, null);
        user.setUserRoles(new HashSet<>(Arrays.asList(TestBuildersUtils.getUserRole(1l, "ROLE_ADMIN"))));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowUserViewPageAsAuthorized_thenOk() throws Exception {
        when(mockUserService.findByName(anyString())).thenReturn(user);
        when(mockUserService.isObjectExist(any())).thenReturn(true);
		mockMvc.perform(get(MAPPED_URL + "/view"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(view().name("userViewPage"));

        when(mockUserService.isObjectExist(any())).thenReturn(false);
		mockMvc.perform(get(MAPPED_URL + "/view"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("mainPage"));
    }


    @Test
    public void whenShowUserViewPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/view"))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void whenChangeUserPasswordAsAuthorized_thenOk() throws Exception {
        when(mockUserService.findById(anyLong())).thenReturn(user);
        when(mockUserService.findByName(anyString())).thenReturn(user);
        when(mockUserService.isObjectExist(any())).thenReturn(true);
		mockMvc.perform(get(MAPPED_URL + "/changePassword-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token=*"));
                //.andExpect(redirectedUrlPattern("/passwordReset?token=^[a-zA-Z0-9]+$"));

        User wrongUser = new User();
        when(mockUserService.findById(anyLong())).thenReturn(wrongUser);
        when(mockUserService.findByName(anyString())).thenReturn(wrongUser);
        when(mockUserService.isObjectExist(any())).thenReturn(false);
		mockMvc.perform(get(MAPPED_URL + "/changePassword-" + wrongUser.getId()))
                //.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenChangeUserPasswordAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/changePassword-" + user.getId()))
                //.andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }
}