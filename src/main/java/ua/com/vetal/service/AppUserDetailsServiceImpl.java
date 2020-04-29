package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.vetal.dao.AppRoleDAO;
import ua.com.vetal.dao.AppUserDAO;
import ua.com.vetal.entity.AppUser;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AppUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser appUser = this.appUserDAO.findUserAccount(userName);

		if (appUser == null) {
			throw new UsernameNotFoundException("AppUser " + userName + " was not found in the database");
		}
		// [ROLE_ADMIN, ROLE_ACCOUNTANT, ROLE_MANAGER,..]
		List<String> roleNames = this.appRoleDAO.getRoleNames(appUser.getUserId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_ADMIN, ROLE_ACCOUNTANT, ROLE_MANAGER..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = new User(appUser.getUserName(),
				appUser.getEncryptedPassword(), grantList);

		return userDetails;
	}
}
