package ua.com.vetal;

import ua.com.vetal.entity.AppUser;

public class TestDataUtils {

	public static AppUser getAppUser() {
		AppUser appUser = new AppUser();
		appUser.setUserName("AppUser");
		appUser.setEncryptedPassword("password");
		appUser.setEnabled(true);
		return appUser;
	}
}
