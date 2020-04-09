package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordForgotControllerTest {

    public static final String MAPPED_URL = "/forgotPassword";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl mockUserService;
    @MockBean
    private PasswordResetTokenRepository tokenRepository;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = TestBuildersUtils.getUser(1l, "New Name", "", true, null);
        user.setUserRoles(new HashSet<>(Arrays.asList(TestBuildersUtils.getUserRole(1l, "ROLE_ADMIN"))));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetPasswordResetPageAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", notNullValue()))
                .andExpect(model().attribute("title", "Forgot Password"))
                .andExpect(view().name("passwordForgotPage"));
    }

    /*@Test
    public void whenGetPasswordResetPageAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", notNullValue()))
                .andExpect(model().attribute("title", "Forgot Password"))
                .andExpect(view().name("passwordForgotPage"));
    }*/

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenPostPasswordResetPageAsAuthorized_thenOk() throws Exception {

        when(mockUserService.findByName(anyString())).thenReturn(user);
        mockMvc.perform(post(MAPPED_URL).param("userName", "user"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token=*"));
    }

   /* @Test
    public void whenPostPasswordResetPageAsNoAuthorized_thenOk() throws Exception {
        when(mockUserService.findByName(anyString())).thenReturn(user);
        mockMvc.perform(post(MAPPED_URL).param("userName", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("forgotPasswordForm", notNullValue()))
                .andExpect(redirectedUrlPattern("/passwordReset?token=*"));
    }*/

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenPostPasswordResetPageWithEmptyUserAsAuthorized_thenError() throws Exception {

        //when(mockUserService.findByName(anyString())).thenReturn(user);
        mockMvc.perform(post(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attribute("forgotPasswordForm", notNullValue()))
                .andExpect(model().attributeHasFieldErrors("forgotPasswordForm", "userName"))
                .andExpect(view().name("passwordForgotPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenPostPasswordResetPageWithNotFoundUserAsAuthorized_thenError() throws Exception {

        when(mockUserService.findByName(anyString())).thenReturn(null);
        mockMvc.perform(post(MAPPED_URL)
                .param("userName", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("forgotPasswordForm", notNullValue()))
                .andExpect(model().attributeHasFieldErrors("forgotPasswordForm", "userName"))
                .andExpect(view().name("passwordForgotPage"));
    }

    /*@Test
    public void whenPostPasswordResetPageWithEmptyUserAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        when(mockUserService.findByName(anyString())).thenReturn(user);
        mockMvc.perform(post(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("passwordForgotPage"));
    }*/

}