package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.AppUser;
import ua.com.vetal.entity.User;
import ua.com.vetal.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl mockUserService;

    private AppUser appUser;

    @BeforeEach
    public void beforeEach() {
        appUser = TestDataUtils.getAppUser();
    }

    @Test
    void personList() throws Exception {
        //when(mockUserService.findAllObjects()).thenReturn(Arrays.asList(appUser));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
        //.andExpect(content().json("{\"id\":1,\"title\":\"delectus aut autem\",\"userId\":1,\"completed\":false}"))
        ;
    }

    @Test
    void showAddUserPage() {
    }

    @Test
    void editUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void resetUserPassword() {
    }

    @Test
    void initializeRoles() {
    }

    @Test
    void initializeTitle() {
    }
}