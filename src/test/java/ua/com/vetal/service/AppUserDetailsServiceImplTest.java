package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.dao.AppRoleDAO;
import ua.com.vetal.dao.AppUserDAO;
import ua.com.vetal.entity.AppUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppUserDetailsServiceImplTest {
	@Autowired
	private AppUserDetailsServiceImpl appUserDetailsService;
	@MockBean
	private AppUserDAO appUserDAO;
	@MockBean
	private AppRoleDAO appRoleDAO;

	private AppUser appUser;

	@BeforeEach
	public void beforeEach() {
		appUser = TestBuildersUtils.getAppUser();
	}

	@Test
	void whenFindById_thenReturnUser() {
		when(appUserDAO.findUserAccount(appUser.getUserName())).thenReturn(appUser);
		List<String> roleNames = Arrays.asList("role1", "role2");
		when(appRoleDAO.getRoleNames(appUser.getUserId())).thenReturn(roleNames);

		UserDetails userDetails = appUserDetailsService.loadUserByUsername(appUser.getUserName());
		assertNotNull(userDetails);
		assertEquals(appUser.getUserName(), userDetails.getUsername());
		assertEquals(roleNames.size(), userDetails.getAuthorities().size());
	}

	@Test
	void whenFindByIdNotFound_thenThrow() {
		when(appUserDAO.findUserAccount(appUser.getUserName())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> {
			appUserDetailsService.loadUserByUsername(appUser.getUserName());
		});
	}
}