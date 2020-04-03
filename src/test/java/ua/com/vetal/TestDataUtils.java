package ua.com.vetal;

import ua.com.vetal.entity.*;

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

    public static Client getClient(Long id, String fullName, String firstName, String lastName, String middleName, String address, String email, String phone) {
        Client client = new Client();
        client.setId(id);
        client.setFullName(fullName);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setMiddleName(middleName);
        client.setAddress(address);
        client.setEmail(email);
        client.setPhone(phone);
        return client;
    }

    public static Manager getManager(String firstName, String lastName, String middleName, String email) {
        Manager manager = new Manager();
        //Manager.setId(1l);
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setMiddleName(middleName);
        //manager.setShortName(shortName);
        //manager.setCorpName(corpName);
        manager.setEmail(email);
        //manager.setPhone(phone);
        return manager;
    }
}
