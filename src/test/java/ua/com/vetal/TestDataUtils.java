package ua.com.vetal;

import ua.com.vetal.entity.*;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataUtils {
    public static Manager getManager(Long id) {
        Manager manager = TestBuildersUtils.getManager(id, "firstName" + id,
                "lastName" + id, "middleName" + id, "email" + id);
        return manager;
    }

    public static Printer getPrinter(Long id) {
        Printer printer = TestBuildersUtils.getPrinter(id, "firstName" + id,
                "lastName" + id, "middleName" + id, "email" + id);
        return printer;
    }

    public static Worker getWorker(Long id) {
        Worker worker = TestBuildersUtils.getWorker(id, "firstName" + id,
                "lastName" + id, "middleName" + id, "email" + id);
        return worker;
    }

    public static Client getClient(Long id) {
        Client client = TestBuildersUtils.getClient(id, "fullName" + id,
                "firstName" + id, "lastName" + id, "middleName" + id,
                "address" + id, "email" + id, "phone" + id);
        client.setManager(getManager(id));
        return client;
    }

    public static Contractor getContractor(Long id) {
        Contractor contractor = TestBuildersUtils.getContractor(1l, "corpName" + id, "shortName" + id,
                "firstName" + id, "lastName" + id, "middleName" + id, "address" + id,
                "email" + id, "phone" + id, "siteURL" + id);
        contractor.setManager(getManager(id));
        return contractor;
    }

    public static LinkType getLinkType(Long id) {
        LinkType linkType = TestBuildersUtils.getLinkType(id, "linkType" + id);
        return linkType;
    }

    public static Link getLink(Long id) {
        Link link = TestBuildersUtils.getLink(id, "fullName" + id, "shortName" + id, getLinkType(id), "path" + id);
        return link;
    }

    public static ProductionTypeDirectory getProductionTypeDirectory(Long id) {
        ProductionTypeDirectory productionTypeDirectory = TestBuildersUtils.getProductionTypeDirectory(id, "ProductionTypeDirectory" + id);
        return productionTypeDirectory;
    }

    public static ProductionDirectory getProductionDirectory(Long id) {
        ProductionDirectory productionDirectory = TestBuildersUtils.getProductionDirectory(id,
                "fullName" + id, "shortName" + id, getProductionTypeDirectory(id));
        return productionDirectory;
    }

    public static Order getOrder(Long id) {
        double amount = ThreadLocalRandom.current().nextDouble(1000, 5000);
        double debtAmount = ThreadLocalRandom.current().nextDouble(1000, amount);
        int printing = ThreadLocalRandom.current().nextInt(1, 1000);
        Order order = TestBuildersUtils.getOrder(id, amount, getClient(id),
                new Date(), debtAmount, "fullNumber" + id, getManager(id), "orderType" + id,
                printing, getProductionDirectory(id));
        return order;

    }
}
