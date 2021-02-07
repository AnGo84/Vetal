package ua.com.vetal;

import ua.com.vetal.entity.*;

import java.util.Date;
import java.util.Set;

public class TestBuildersUtils {

    public static AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setUserName("AppUser");
        appUser.setEncryptedPassword("password");
        appUser.setEnabled(true);
        return appUser;
    }

    public static User getUser(Long id, String name, String pass, boolean isActive, Set<UserRole> userRoleSet) {
        User user = new User();
        user.setId(id);
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

    public static ChromaticityDirectory getChromaticityDirectory(Long id, String name) {
        ChromaticityDirectory directory = new ChromaticityDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static CringleDirectory getCringleDirectory(Long id, String name) {
        CringleDirectory directory = new CringleDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static FormatDirectory getFormatDirectory(Long id, String name) {
        FormatDirectory directory = new FormatDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static LaminateDirectory getLaminateDirectory(Long id, String name) {
        LaminateDirectory directory = new LaminateDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static NumberBaseDirectory getNumberBaseDirectory(Long id, String name) {
        NumberBaseDirectory directory = new NumberBaseDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static PaperDirectory getPaperDirectory(Long id, String name) {
        PaperDirectory directory = new PaperDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static PrintingUnitDirectory getPrintingUnitDirectory(Long id, String name) {
        PrintingUnitDirectory directory = new PrintingUnitDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static ProductionTypeDirectory getProductionTypeDirectory(Long id, String name) {
        ProductionTypeDirectory directory = new ProductionTypeDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }

    public static StockDirectory getStockDirectory(Long id, String name) {
        StockDirectory directory = new StockDirectory();
        directory.setId(id);
        directory.setName(name);
        return directory;
    }


    public static Payment getPayment(Long id, String name, String altName) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setName(name);
        payment.setAltName(altName);
        return payment;
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

    public static State getState(Long id, String name, String altName) {
        State state = new State();
        state.setId(id);
        state.setName(name);
        state.setAltName(altName);
        return state;
    }

    public static Client getClient(Long id, String corpName, String firstName, String lastName, String middleName, String address, String email, String phone) {
		Client client = new Client();
		client.setId(id);
		client.setCorpName(corpName);
		client.setFirstName(firstName);
		client.setLastName(lastName);
		client.setMiddleName(middleName);
		client.setAddress(address);
		client.setEmail(email);
		client.setPhone(phone);
		return client;
    }

	public static Contractor getContractor(Long id, String corpName, String firstName, String lastName, String middleName, String address, String email, String phone, String siteURL) {
		Contractor contractor = new Contractor();
		contractor.setId(id);
		contractor.setCorpName(corpName);
		contractor.setFirstName(firstName);
		contractor.setLastName(lastName);
		contractor.setMiddleName(middleName);
		contractor.setAddress(address);
		contractor.setEmail(email);
		contractor.setPhone(phone);
		contractor.setSiteURL(siteURL);
        return contractor;
    }

    public static Manager getManager(Long id, String firstName, String lastName, String middleName, String email) {
        Manager manager = new Manager();
        manager.setId(id);
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setMiddleName(middleName);
        manager.setEmail(email);
        return manager;
    }

    public static Printer getPrinter(Long id, String firstName, String lastName, String middleName, String email) {
        Printer printer = new Printer();
        printer.setId(id);
        printer.setFirstName(firstName);
        printer.setLastName(lastName);
        printer.setMiddleName(middleName);
        printer.setEmail(email);
        return printer;
    }

    public static Worker getWorker(Long id, String firstName, String lastName, String middleName, String email) {
        Worker worker = new Worker();
        worker.setId(id);
        worker.setFirstName(firstName);
        worker.setLastName(lastName);
        worker.setMiddleName(middleName);
        worker.setEmail(email);
        return worker;
    }

    public static ProductionDirectory getProductionDirectory(Long id, String fullName, String shortName, ProductionTypeDirectory productionTypeDirectory) {
        ProductionDirectory printer = new ProductionDirectory();
        printer.setId(id);
        printer.setFullName(fullName);
        printer.setShortName(shortName);
        printer.setProductionType(productionTypeDirectory);
        return printer;
    }

    public static StatisticOrder getOrder(Long id, double amount, Client client, Date dateBegin, double debtAmount, String fullNumber, Manager manager, String orderType, int printing, ProductionDirectory production) {
        StatisticOrder statisticOrder = new StatisticOrder();
        statisticOrder.setId(id);
        statisticOrder.setAmount(amount);
        statisticOrder.setClient(client);
        statisticOrder.setDateBegin(dateBegin);
        statisticOrder.setDebtAmount(debtAmount);
        statisticOrder.setFullNumber(fullNumber);
        statisticOrder.setManager(manager);
        statisticOrder.setOrderType(orderType);
        statisticOrder.setPrinting(printing);
        statisticOrder.setProduction(production);
        return statisticOrder;
    }


    public static StatisticOrder getOrderFromTask(Task task) {
        StatisticOrder statisticOrder = new StatisticOrder();
        statisticOrder.setId(task.getId());
        statisticOrder.setAmount(task.getAmountForContractor());
        statisticOrder.setClient(task.getClient());
        statisticOrder.setDateBegin(task.getDateBegin());
        statisticOrder.setDebtAmount(task.getDebtAmount());
        statisticOrder.setFullNumber(task.getFullNumber());
        statisticOrder.setManager(task.getManager());
        statisticOrder.setOrderType("task");
        statisticOrder.setPrinting(task.getPrinting());
        statisticOrder.setProduction(task.getProduction());
        return statisticOrder;
    }

    public static StatisticOrder getOrderFromStencil(Stencil stencil) {
        StatisticOrder statisticOrder = new StatisticOrder();
        statisticOrder.setId(stencil.getId());
        statisticOrder.setAmount(stencil.getAmount());
        statisticOrder.setClient(stencil.getClient());
        statisticOrder.setDateBegin(stencil.getDateBegin());
        statisticOrder.setDebtAmount(stencil.getDebtAmount());
        statisticOrder.setFullNumber(stencil.getFullNumber());
        statisticOrder.setManager(stencil.getManager());
        statisticOrder.setOrderType("stencil");
        statisticOrder.setPrinting(stencil.getPrinting());
        statisticOrder.setProduction(stencil.getProduction());
        return statisticOrder;
    }
}
