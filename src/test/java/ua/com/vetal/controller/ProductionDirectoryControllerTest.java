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
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;

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
public class ProductionDirectoryControllerTest {
    public static final String MAPPED_URL = "/productions";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductionDirectoryServiceImpl mockProductionDirectoryService;

    private ProductionTypeDirectory productionType;
    private ProductionDirectory production;

    @BeforeEach
    public void beforeEach() {
		/*productionType = TestBuildersUtils.getProductionTypeDirectory(1l,"Production type");
		production = TestBuildersUtils.getProductionDirectory(1l, "fullName", "shortName", productionType);*/
        production = TestDataUtils.getProductionDirectory(1l);
        productionType = production.getProductionType();

        when(mockProductionDirectoryService.findAllObjects()).thenReturn(Arrays.asList(production));
        when(mockProductionDirectoryService.findById(anyLong())).thenReturn(production);
        when(mockProductionDirectoryService.findByName(anyString())).thenReturn(production);
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
    public void whenGetProductionDirectoryListAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("productionsList", notNullValue()))
                .andExpect(view().name("productionsPage"));

        mockMvc.perform(get(MAPPED_URL + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("productionsList", notNullValue()))
                .andExpect(view().name("productionsPage"));
    }

    @Test
    public void whenGetProductionDirectoryListAsNoAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenShowAddProductionDirectoryPageAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("production"))
                .andExpect(model().attribute("production", notNullValue()))
                .andExpect(model().attribute("edit", false))
                .andExpect(view().name("productionPage"));
    }


    @Test
    public void whenShowAddProductionDirectoryPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenEditProductionDirectoryAsAuthorized_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + production.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("production"))
                .andExpect(model().attribute("production", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(view().name("productionPage"));
    }

    @Test
    public void whenEditProductionDirectoryAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/edit-" + production.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateProductionDirectoryAsAuthorizedWithNullProductionDirectory_thenOk() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("production"))
                .andExpect(model().attribute("production", notNullValue()))
                .andExpect(model().attribute("production", hasProperty("id", nullValue())))
                .andExpect(model().attribute("production", hasProperty("fullName", blankOrNullString())))
                .andExpect(model().attribute("production", hasProperty("shortName", blankOrNullString())))
                .andExpect(model().attribute("production", hasProperty("productionType", blankOrNullString())))
                .andExpect(view().name("productionPage"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenUpdateProductionDirectoryAsAuthorizedWithNotNullProductionDirectory_thenOk() throws Exception {
        //doNothing().when(mockUserService).updateObject(any(User.class));
        mockProductionDirectoryService.updateObject(production);

        mockMvc.perform(post(MAPPED_URL + "/update")
                .param("id", String.valueOf(production.getId()))
                .param("fullName", production.getFullName())
                .param("shortName", production.getShortName())
                .param("productionType", String.valueOf(productionType.getId()))
        )
                .andDo(print())
                .andExpect(status().isFound())

                .andExpect(redirectedUrl(MAPPED_URL));
        verify(mockProductionDirectoryService, times(1)).updateObject(production);
    }


    @Test
    public void whenUpdateProductionDirectoryAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(post(MAPPED_URL + "/update"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void whenDeleteProductionDirectoryAsAuthorizedWithNotNullUser_thenOk() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + production.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAPPED_URL));

        verify(mockProductionDirectoryService, times(1)).deleteById(production.getId());
    }

    @Test
    public void whenDeleteProductionDirectoryAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get(MAPPED_URL + "/delete-" + production.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
    }

}