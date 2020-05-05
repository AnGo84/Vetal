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

    public static StatisticOrder getOrder(Long id) {
		double amount = ThreadLocalRandom.current().nextDouble(1000, 5000);
		double debtAmount = ThreadLocalRandom.current().nextDouble(1000, amount);
		int printing = ThreadLocalRandom.current().nextInt(1, 1000);
		StatisticOrder statisticOrder = TestBuildersUtils.getOrder(id, amount, getClient(id),
				new Date(), debtAmount, "fullNumber" + id, getManager(id), "orderType" + id,
				printing, getProductionDirectory(id));
		return statisticOrder;

	}

    public static ViewTask getViewTask(Long id, int taskNumber) {
        ViewTask viewTask = new ViewTask();
        viewTask.setId(id);
        viewTask.setNumber(taskNumber);
        viewTask.setNumberBase(TestBuildersUtils.getNumberBaseDirectory(id, "numberBaseTask" + taskNumber));
        viewTask.setNumberSuffix(taskNumber);
        viewTask.setFullNumber("fullNumber" + taskNumber);
        viewTask.setAccount("account" + taskNumber);
        Manager manager = TestBuildersUtils.getManager(id, "managerFirstName" + taskNumber, "managerLastName" + taskNumber, "managerMiddleName" + taskNumber, "managerEmail" + taskNumber);
        viewTask.setManager(manager);
        viewTask.setWorkName("workName" + taskNumber);
        viewTask.setFileName("fileName" + taskNumber);

		/*ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(id, "Production type Task" + taskNumber);
		ProductionDirectory production = TestBuildersUtils.getProductionDirectory(id, "fullName" + taskNumber, "shortName" + taskNumber, productionType);
		viewTask.setProduction(production);
		viewTask.setProductionType(productionType);*/

        viewTask.setDateBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -10 * taskNumber));
        viewTask.setDateEnd(DateUtils.addToDate(new Date(), Calendar.DATE, -1 * taskNumber));

        Client client = TestBuildersUtils.getClient(id, "fullName" + taskNumber, "firstName" + taskNumber, "lastName" + taskNumber, "middleName" + taskNumber, "address" + taskNumber, "email" + taskNumber, "phone" + taskNumber);
        client.setManager(manager);
        viewTask.setClient(client);

        boolean taskTrueFalse = (taskNumber % 2) == 0;
        viewTask.setAmount(20 * taskNumber);
        viewTask.setState(TestBuildersUtils.getState(id, "name task" + taskNumber, "altname" + taskNumber));
        viewTask.setPayment(TestBuildersUtils.getPayment(id, "name task" + taskNumber, "altname" + taskNumber));
        viewTask.setDebtAmount(5 * taskNumber);

        return viewTask;
    }

    public static Task getTask(Long id, int taskNumber) {
        Task task = new Task();
        task.setId(id);
        task.setNumber(taskNumber);
        task.setNumberBase(TestBuildersUtils.getNumberBaseDirectory(id, "numberBaseTask" + taskNumber));
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
		task.setContractorAmount(Double.valueOf(10 * taskNumber));
		task.setProvider("provider" + taskNumber);
		task.setProviderCost(Double.valueOf(8 * taskNumber));
		task.setOtherExpenses(Double.valueOf(3 * taskNumber));

		ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(id, "Production type Task" + taskNumber);
		ProductionDirectory production = TestBuildersUtils.getProductionDirectory(id, "fullName" + taskNumber, "shortName" + taskNumber, productionType);
		task.setProduction(production);
		task.setProductionType(productionType);

		task.setDateBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -10 * taskNumber));
		task.setDateEnd(DateUtils.addToDate(new Date(), Calendar.DATE, -1 * taskNumber));

		Client client = TestBuildersUtils.getClient(id, "fullName" + taskNumber, "firstName" + taskNumber, "lastName" + taskNumber, "middleName" + taskNumber, "address" + taskNumber, "email" + taskNumber, "phone" + taskNumber);
        client.setManager(manager);
        task.setClient(client);
        task.setStock(TestBuildersUtils.getStockDirectory(id, "Stock Task" + taskNumber));
        task.setPrinting(100 * taskNumber);
        task.setPrintingUnit(TestBuildersUtils.getPrintingUnitDirectory(id, "PrintingUnit task" + taskNumber));
        task.setChromaticity(TestBuildersUtils.getChromaticityDirectory(id, "Chromaticity" + taskNumber));

        task.setFormat(TestBuildersUtils.getFormatDirectory(id, "format" + taskNumber));
        task.setPrintingFormat("printing format" + taskNumber);

        task.setLaminate(TestBuildersUtils.getLaminateDirectory(id, "Laminate" + taskNumber));
        task.setPaper(TestBuildersUtils.getPaperDirectory(id, "Paper task" + taskNumber));
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
		task.setAmount(Double.valueOf(20 * taskNumber));
		task.setState(TestBuildersUtils.getState(id, "name task" + taskNumber, "altname" + taskNumber));
		task.setPayment(TestBuildersUtils.getPayment(id, "name task" + taskNumber, "altname" + taskNumber));
		task.setDebtAmount(Double.valueOf(5 * taskNumber));

		return task;
	}

	public static Stencil getStencil(Long id, int number) {
		Stencil stencil = new Stencil();
		stencil.setId(id);
		stencil.setNumber(number);
		stencil.setNumberBase(TestBuildersUtils.getNumberBaseDirectory(id, "numberBaseStencil" + number));
		stencil.setNumberSuffix(number);
		stencil.setFullNumber("fullNumber" + number);
		stencil.setAccount("account" + number);

		Manager manager = TestBuildersUtils.getManager(id, "managerFirstName" + number, "managerLastName" + number, "managerMiddleName" + number, "managerEmail" + number);
		stencil.setManager(manager);
		stencil.setOrderName("orderName" + number);

		Client client = TestBuildersUtils.getClient(id, "fullName" + number, "firstName" + number, "lastName" + number, "middleName" + number, "address" + number, "email" + number, "phone" + number);
		client.setManager(manager);
		stencil.setClient(client);

		stencil.setDateBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -10 * number));
		stencil.setDateEnd(DateUtils.addToDate(new Date(), Calendar.DATE, -1 * number));

		ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(id, "Production type Stencil" + number);
		ProductionDirectory production = TestBuildersUtils.getProductionDirectory(id, "fullName" + number, "shortName" + number, productionType);
		stencil.setProduction(production);

		stencil.setStock(TestBuildersUtils.getStockDirectory(id, "Stock Stencil" + number));

		stencil.setPrinting(100 * number);
		stencil.setPrintingUnit(TestBuildersUtils.getPrintingUnitDirectory(id, "PrintingUnit Stencil" + number));

		stencil.setAdjustment("Adjustment" + number);
		stencil.setPaper(TestBuildersUtils.getPaperDirectory(id, "Paper Stencil" + number));
		stencil.setPaperFormat("PaperFormat" + number);
		stencil.setSheetNumber(2 * number);

		stencil.setPrinter(TestBuildersUtils.getPrinter(id, "PrinterFirstName" + number, "PrinterLastName" + number, "PrinterMiddleName" + number, "PrinterEmail" + number));
		stencil.setDatePrintBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -5 * number));
		stencil.setPrintingNote("Stencil printing description" + number);

		stencil.setWorkerPrint(TestBuildersUtils.getWorker(id, "WorkerFirstName" + number, "WorkerLastName" + number, "WorkerMiddleName" + number, "WorkerEmail" + number));
		stencil.setWorkerCut(stencil.getWorkerPrint());

		boolean stencilTrueFalse = (number % 2) == 0;
		stencil.setFillet(stencilTrueFalse);
		stencil.setPopup(stencilTrueFalse);
		stencil.setCarving(stencilTrueFalse);
		stencil.setStamping(stencilTrueFalse);
		stencil.setEmbossing(stencilTrueFalse);
		stencil.setBending(stencilTrueFalse);
		stencil.setPlotter(stencilTrueFalse);

		stencil.setPackBox(stencilTrueFalse);
		stencil.setPackPellicle(stencilTrueFalse);
		stencil.setPackPaper(stencilTrueFalse);
		stencil.setPackPackage(stencilTrueFalse);
		stencil.setPackNP(stencilTrueFalse);

		stencil.setNumeration(stencilTrueFalse);
		stencil.setNumerationStart(number + 1);
		stencil.setCutRibbon(stencilTrueFalse);
		stencil.setRibbonLength(8 * number);
		stencil.setSticker(stencilTrueFalse);
		stencil.setAmount(Double.valueOf(20 * number));
		stencil.setState(TestBuildersUtils.getState(id, "name stencil" + number, "altname" + number));
		stencil.setPayment(TestBuildersUtils.getPayment(id, "name stencil" + number, "altname" + number));
		stencil.setDebtAmount(Double.valueOf(5 * number));
		stencil.setKraskoottisk(8 * number);
		stencil.setCostOfMaterials(Double.valueOf(15 * number));
		stencil.setCostOfPrinting(Double.valueOf(2 * number));
		stencil.setOtherExpenses(Double.valueOf(3 * number));

		return stencil;
	}
}
