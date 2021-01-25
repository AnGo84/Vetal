package ua.com.vetal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.repositories.FormatDirectoryRepositoryTest;
import ua.com.vetal.service.FormatDirectoryServiceImpl;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FormatDirectoryControllerTest {
	public static final String MAPPED_URL = "/format";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FormatDirectoryServiceImpl mockDirectoryService;

	private FormatDirectory directory;

	@BeforeEach
	public void beforeEach() {
		directory = TestBuildersUtils.getFormatDirectory(1l, FormatDirectoryRepositoryTest.DIRECTORY_NAME);

		when(mockDirectoryService.getAll()).thenReturn(Arrays.asList(directory));
		when(mockDirectoryService.get(anyLong())).thenReturn(directory);
	}


	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_MANAGER"})
	public void whenGetDirectoryListAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("directoryList", notNullValue()))
				.andExpect(view().name("directoryPage"));

		mockMvc.perform(get(MAPPED_URL + "/"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("directoryList", notNullValue()))
				.andExpect(view().name("directoryPage"));

		mockMvc.perform(get(MAPPED_URL + "/list"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attribute("directoryList", notNullValue()))
				.andExpect(view().name("directoryPage"));
	}

	@Test
	public void whenGetDirectoryListAsNoAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL))
				//.andDo
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenGetShowAddRecordPageAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("directory"))
				.andExpect(model().attribute("directory", notNullValue()))
				.andExpect(model().attribute("edit", false))
				.andExpect(view().name("directoryRecordPage"));
	}


	@Test
	public void whenGetShowAddRecordPageAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/add"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenEditRecordAsAuthorized_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + directory.getId()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("directory"))
				.andExpect(model().attribute("directory", notNullValue()))
				.andExpect(model().attribute("edit", true))
				.andExpect(view().name("directoryRecordPage"));
	}

	@Test
	public void whenEditRecordAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/edit-" + directory.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateRecordAsAuthorizedWithNullDirectory_thenOk() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("directory"))
				.andExpect(model().attribute("directory", notNullValue()))
				.andExpect(model().attribute("directory", hasProperty("id", nullValue())))
				.andExpect(model().attribute("directory", hasProperty("name", blankOrNullString())))
				.andExpect(view().name("directoryRecordPage"));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateRecordAsAuthorizedWithNotNullDirectory_thenOk() throws Exception {
		//doNothing().when(mockUserService).updateObject(any(User.class));
		//mockDirectoryService.update(directory);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(directory.getId()))
				.param("name", directory.getName()))
				//.andDo
				.andExpect(status().isFound())

				.andExpect(redirectedUrl(MAPPED_URL));
		verify(mockDirectoryService, times(1)).update(directory);
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenUpdateRecordAsAuthorizedWithExistName_thenError() throws Exception {
		when(mockDirectoryService.isExist(any())).thenReturn(true);
		//mockDirectoryService.updateObject(directory);

		mockMvc.perform(post(MAPPED_URL + "/update")
				.param("id", String.valueOf(directory.getId()))
				.param("name", directory.getName()))
				//.andDo
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("directory"))
				.andExpect(model().attributeHasFieldErrors("directory", "name"))
				.andExpect(view().name("directoryRecordPage"));
		verify(mockDirectoryService, times(0)).update(directory);
	}

	@Test
	public void whenUpdateRecordAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(post(MAPPED_URL + "/update"))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}

	@Test
	@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
	public void whenDeleteRecordAsAuthorizedWithNotNullDirectory_thenOk() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + directory.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(MAPPED_URL));

		verify(mockDirectoryService, times(1)).deleteById(directory.getId());
	}

	@Test
	public void whenDeleteRecordAsNoAuthorized_thenRedirectToLoginPage() throws Exception {
		mockMvc.perform(get(MAPPED_URL + "/delete-" + directory.getId()))
				//.andDo
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(TestControllerUtils.HTTP_LOCALHOST_LOGIN_URL));
	}
}