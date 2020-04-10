package ua.com.vetal;

import ua.com.vetal.entity.*;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
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

    public static Task getTask(Long id, int taskNumber) {
        Task task = new Task();
        task.setId(id);
        task.setNumber(taskNumber);
        task.setNumberBase(TestBuildersUtils.getNumberBaseDirectory(id, "numberBaseDirectory" + taskNumber));
        task.setNumberSuffix(taskNumber);
        task.setFullNumber("fullNumber" + taskNumber);
        task.setAccount("account" + taskNumber);
        Manager manager = TestBuildersUtils.getManager(id, "managerFirstName" + taskNumber, "managerLastName" + taskNumber, "managerMiddleName" + taskNumber, "managerEmail" + taskNumber);
        task.setManager(manager);
        task.setWorkName("workName" + taskNumber);
        task.setFileName("fileName" + taskNumber);
        task.setDbFile(new DBFile("file" + taskNumber, "content_type" + taskNumber, ("file data" + taskNumber).getBytes()));

        Contractor contractor = TestBuildersUtils.getContractor(id, "corpName", "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
        contractor.setManager(manager);
        task.setContractor(contractor);
        task.setContractorNumber("ContractorNumber" + taskNumber);
        task.setContractorAmount(10 * taskNumber);
        task.setProvider("provider" + taskNumber);
        task.setProviderCost(8 * taskNumber);
        task.setOtherExpenses(3 * taskNumber);

        ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(id, "Production type" + taskNumber);
        ProductionDirectory production = TestBuildersUtils.getProductionDirectory(id, "fullName" + taskNumber, "shortName" + taskNumber, productionType);
        task.setProduction(production);
        task.setProductionType(productionType);

        task.setDateBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -10 * taskNumber));
        task.setDateEnd(DateUtils.addToDate(new Date(), Calendar.DATE, -1 * taskNumber));

        Client client = TestBuildersUtils.getClient(id, "fullName" + taskNumber, "firstName" + taskNumber, "lastName" + taskNumber, "middleName" + taskNumber, "address" + taskNumber, "email" + taskNumber, "phone" + taskNumber);
        client.setManager(manager);
        task.setClient(client);
        task.setStock(TestBuildersUtils.getStockDirectory(id, "Stock" + taskNumber));
        task.setPrinting(100 * taskNumber);
        task.setPrintingUnit(TestBuildersUtils.getPrintingUnitDirectory(id, "PrintingUnit" + taskNumber));
        task.setChromaticity(TestBuildersUtils.getChromaticityDirectory(id, "Chromaticity" + taskNumber));

        task.setFormat(TestBuildersUtils.getFormatDirectory(id, "format" + taskNumber));
        task.setPrintingFormat("printing format" + taskNumber);

        task.setLaminate(TestBuildersUtils.getLaminateDirectory(id, "Laminate" + taskNumber));
        task.setPaper(TestBuildersUtils.getPaperDirectory(id, "Paper" + taskNumber));
        task.setWares("wares" + taskNumber);
        task.setCringle(TestBuildersUtils.getCringleDirectory(id, "Cringle" + taskNumber));
        boolean taskTrueFalse = (taskNumber % 2) == 0;
        task.setFillet(taskTrueFalse);
        task.setPopup(taskTrueFalse);
        task.setCarving(taskTrueFalse);
        task.setStamping(taskTrueFalse);
        task.setEmbossing(taskTrueFalse);
        task.setBending(taskTrueFalse);
        task.setPlotter(taskTrueFalse);
        task.setAssembly(taskTrueFalse);
        task.setCutting(taskTrueFalse);
        task.setNote("Task description" + taskNumber);
        task.setPackBox(taskTrueFalse);
        task.setPackPellicle(taskTrueFalse);
        task.setPackPaper(taskTrueFalse);
        task.setPackPackage(taskTrueFalse);
        task.setPackNP(taskTrueFalse);
        task.setPackBy("Pack by" + taskNumber);
        task.setNumeration(taskTrueFalse);
        task.setNumerationStart(taskNumber + 1);
        task.setAmount(20 * taskNumber);
        task.setState(TestBuildersUtils.getState(id, "name" + taskNumber, "altname" + taskNumber));
        task.setPayment(TestBuildersUtils.getPayment(id, "name" + taskNumber, "altname" + taskNumber));
        task.setDebtAmount(5 * taskNumber);

        return task;
    }
}
