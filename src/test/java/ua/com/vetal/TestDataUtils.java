package ua.com.vetal;

import ua.com.vetal.entity.*;

import java.util.Date;
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
        ChromaticityDirectory directory = new ChromaticityDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

	public static CringleDirectory getCringleDirectory(String name) {
        CringleDirectory directory = new CringleDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static FormatDirectory getFormatDirectory(String name) {
        FormatDirectory directory = new FormatDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static LaminateDirectory getLaminateDirectory(String name) {
        LaminateDirectory directory = new LaminateDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static NumberBaseDirectory getNumberBaseDirectory(String name) {
        NumberBaseDirectory directory = new NumberBaseDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static PaperDirectory getPaperDirectory(String name) {
        PaperDirectory directory = new PaperDirectory();
        //directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static LinkType getLinkType(Long id, String name) {
        LinkType linkType = new LinkType();
        linkType.setId(id);
        linkType.setName(name);
        return linkType;
    }

    public static LinkType getLinkType(String name) {
        return getLinkType(null, name);
    }

    public static Link getLink(Long id, String fullName, String shortName, LinkType linkType, String path) {
        Link link = new Link();
        link.setId(id);
        link.setFullName(fullName);
        link.setShortName(shortName);
        link.setLinkType(linkType);
        link.setPath(path);
        return link;
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

    public static Contractor getContractor(Long id, String corpName, String shortName, String firstName, String lastName, String middleName, String address, String email, String phone, String siteURL) {
        Contractor contractor = new Contractor();
        contractor.setId(id);
        contractor.setCorpName(corpName);
        contractor.setShortName(shortName);
        contractor.setFirstName(firstName);
        contractor.setLastName(lastName);
        contractor.setMiddleName(middleName);
        contractor.setAddress(address);
        contractor.setEmail(email);
        contractor.setPhone(phone);
        contractor.setSiteURL(siteURL);
        return contractor;
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

    public static Order getOrder(Long id, double amount, Client client, Date dateBegin, double debtAmount, String fullNumber, Manager manager, String orderType, int printing, ProductionDirectory production) {
        Order order = new Order();
        order.setId(id);
        order.setAmount(amount);
        order.setClient(client);
        order.setDateBegin(dateBegin);
        order.setDebtAmount(debtAmount);
        order.setFullNumber(fullNumber);
        order.setManager(manager);
        order.setOrderType(orderType);
        order.setPrinting(printing);
        order.setProduction(production);
        return order;
    }
}
