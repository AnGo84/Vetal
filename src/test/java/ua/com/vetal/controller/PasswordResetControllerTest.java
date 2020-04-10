package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;
import ua.com.vetal.handler.PasswordResetTokenHandler;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetControllerTest {
    public static final String MAPPED_URL = "/passwordReset";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl mockUserService;
    @MockBean
    private PasswordResetTokenRepository mockTokenRepository;

    private User user;
    private PasswordResetToken token;

    @BeforeEach
    public void beforeEach() {
        user = TestBuildersUtils.getUser(1l, "New Name", "", true, null);
        user.setUserRoles(new HashSet<>(Arrays.asList(TestBuildersUtils.getUserRole(1l, "ROLE_ADMIN"))));
        token = PasswordResetTokenHandler.getPasswordResetToken(user);
        //when(mockUserService.findByName(anyString())).thenReturn(user);
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetDisplayResetPasswordPage_thenOk() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(get(MAPPED_URL).param("token", "token"))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", notNullValue()))
                .andExpect(model().attribute("token", token.getToken()))
                .andExpect(view().name("passwordResetPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetDisplayResetPasswordPageWhenTokenNull_thenError() throws Exception {
        //when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(get(MAPPED_URL))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("passwordResetPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetDisplayResetPasswordPageWhenTokenNotFound_thenError() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(get(MAPPED_URL))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("passwordResetPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetDisplayResetPasswordPageWhenTokenExpired_thenError() throws Exception {
        token.setExpiryDate(-60);
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(get(MAPPED_URL))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("passwordResetPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenGetDisplayResetPasswordPageWhenTokenExpiredFound_thenError() throws Exception {
        token.setExpiryDate(-60);
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(get(MAPPED_URL))
                //.andDo
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", notNullValue()))
                .andExpect(view().name("passwordResetPage"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenHandlePasswordReset_thenOk() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(post(MAPPED_URL)
                .param("password", "pass")
                .param("confirmPassword", "pass")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?resetSuccess"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenHandlePasswordResetWithoutParams_thenError() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(post(MAPPED_URL))
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenHandlePasswordResetWithOutPassword_thenError() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(post(MAPPED_URL)
                .param("confirmPassword", "pass")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
        mockMvc.perform(post(MAPPED_URL)
                .param("password", "")
                .param("confirmPassword", "pass")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenHandlePasswordResetWithOutConfirmPassword_thenError() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(post(MAPPED_URL)
                .param("password", "pass")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
        mockMvc.perform(post(MAPPED_URL)
                .param("password", "pass")
                .param("confirmPassword", "")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
    }

    @Test
    //@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenHandlePasswordResetPasswordsNorEqual_thenError() throws Exception {
        when(mockTokenRepository.findByToken(anyString())).thenReturn(token);

        mockMvc.perform(post(MAPPED_URL)
                .param("password", "pass")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
        mockMvc.perform(post(MAPPED_URL)
                .param("password", "pass1")
                .param("confirmPassword", "pass2")
                .param("token", "token")
                .param("userId", "1")
        )
                //.andDo
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("/passwordReset?token*"));
    }
}