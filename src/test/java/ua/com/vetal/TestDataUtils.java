package ua.com.vetal;

import ua.com.vetal.entity.AppUser;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestDataUtils {

	public static AppUser getAppUser() {
		AppUser appUser = new AppUser();
		appUser.setUserName("AppUser");
		appUser.setEncryptedPassword("password");
		appUser.setEnabled(true);
		return appUser;
	}
	public static User getUser(String name, String pass, boolean isActive, Set<UserRole> userRoleSet) {
		User user = new User();
		//user.setId(1l);
		user.setName(name);
		user.setEncryptedPassword(pass);
		user.setEnabled(isActive);
		user.setUserRoles(userRoleSet);
		return user;
	}

	public static UserRole getUserRole(Long id, String name) {
		UserRole userRole = new UserRole();
		userRole.setId(id);
		userRole.setName(name);
		return userRole;
	}
	public static ChromaticityDirectory getChromaticityDirectory(String name) {
		ChromaticityDirectory chromaticityDirectory = new ChromaticityDirectory();
		//chromaticityDirectory.setId(id);
		chromaticityDirectory.setName(name);
		return chromaticityDirectory;
	}
}
