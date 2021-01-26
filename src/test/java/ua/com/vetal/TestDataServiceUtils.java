package ua.com.vetal;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.com.vetal.entity.*;

public class TestDataServiceUtils {

    public static Task saveTaskParts(Task task, TestEntityManager entityManager) {
        NumberBaseDirectory numberBaseDirectory = entityManager.persistAndFlush(task.getNumberBase());
        task.setNumberBase(numberBaseDirectory);
        Manager manager = entityManager.persistAndFlush(task.getManager());
        task.setManager(manager);
        DBFile dbFile = entityManager.persistAndFlush(task.getDbFile());
        task.setDbFile(dbFile);
        Contractor contractor = entityManager.persistAndFlush(task.getContractor());
        task.setContractor(contractor);
        ProductionTypeDirectory productionTypeDirectory = entityManager.persistAndFlush(task.getProductionType());
        task.setProductionType(productionTypeDirectory);
        ProductionDirectory productionDirectory = task.getProduction();
        productionDirectory.setProductionType(productionTypeDirectory);
        productionDirectory = entityManager.persistAndFlush(productionDirectory);
        task.setProduction(productionDirectory);
        Client client = task.getClient();
        client.setManager(manager);
        client = entityManager.persistAndFlush(client);
        task.setClient(client);
        StockDirectory stockDirectory = entityManager.persistAndFlush(task.getStock());
        task.setStock(stockDirectory);
        PrintingUnitDirectory printingUnitDirectory = entityManager.persistAndFlush(task.getPrintingUnit());
        task.setPrintingUnit(printingUnitDirectory);
        ChromaticityDirectory chromaticityDirectory = entityManager.persistAndFlush(task.getChromaticity());
        task.setChromaticity(chromaticityDirectory);
        FormatDirectory formatDirectory = entityManager.persistAndFlush(task.getFormat());
        task.setFormat(formatDirectory);
        LaminateDirectory laminateDirectory = entityManager.persistAndFlush(task.getLaminate());
        task.setLaminate(laminateDirectory);
		PaperDirectory paperDirectory = entityManager.persistAndFlush(task.getPaper());
		task.setPaper(paperDirectory);
		CringleDirectory cringleDirectory = entityManager.persistAndFlush(task.getCringle());
		task.setCringle(cringleDirectory);
		State state = entityManager.persistAndFlush(task.getState());
		task.setState(state);
		Payment payment = entityManager.persistAndFlush(task.getPayment());
		task.setPayment(payment);
		return task;
	}

	public static Stencil saveStencilParts(Stencil stencil, TestEntityManager entityManager) {
		NumberBaseDirectory numberBaseDirectory = entityManager.persistAndFlush(stencil.getNumberBase());
		stencil.setNumberBase(numberBaseDirectory);
		Manager manager = entityManager.persistAndFlush(stencil.getManager());
		stencil.setManager(manager);
		Client client = stencil.getClient();
		client.setManager(manager);
		client = entityManager.persistAndFlush(client);
		stencil.setClient(client);

		ProductionTypeDirectory productionTypeDirectory = entityManager.persistAndFlush(stencil.getProduction().getProductionType());
		ProductionDirectory productionDirectory = stencil.getProduction();
		productionDirectory.setProductionType(productionTypeDirectory);
		productionDirectory = entityManager.persistAndFlush(productionDirectory);
		stencil.setProduction(productionDirectory);

		StockDirectory stockDirectory = entityManager.persistAndFlush(stencil.getStock());
		stencil.setStock(stockDirectory);
		PrintingUnitDirectory printingUnitDirectory = entityManager.persistAndFlush(stencil.getPrintingUnit());
		stencil.setPrintingUnit(printingUnitDirectory);

		PaperDirectory paperDirectory = entityManager.persistAndFlush(stencil.getPaper());
		stencil.setPaper(paperDirectory);

		Printer printer = entityManager.persistAndFlush(stencil.getPrinter());
		stencil.setPrinter(printer);

        Worker worker = entityManager.persistAndFlush(stencil.getWorkerPrint());
        stencil.setWorkerPrint(worker);
        stencil.setWorkerCut(worker);

        State state = entityManager.persistAndFlush(stencil.getState());
        stencil.setState(state);
        Payment payment = entityManager.persistAndFlush(stencil.getPayment());
        stencil.setPayment(payment);
        return stencil;
    }


    public static ViewTask saveViewTaskParts(ViewTask viewTask, TestEntityManager entityManager) {
        NumberBaseDirectory numberBaseDirectory = entityManager.persistAndFlush(viewTask.getNumberBase());
        viewTask.setNumberBase(numberBaseDirectory);
        Manager manager = entityManager.persistAndFlush(viewTask.getManager());
        viewTask.setManager(manager);
        Contractor contractor = entityManager.persistAndFlush(viewTask.getContractor());
        viewTask.setContractor(contractor);

        ProductionTypeDirectory productionTypeDirectory = entityManager.persistAndFlush(viewTask.getProduction().getProductionType());

        ProductionDirectory productionDirectory = viewTask.getProduction();
        productionDirectory.setProductionType(productionTypeDirectory);
        productionDirectory = entityManager.persistAndFlush(productionDirectory);
        viewTask.setProduction(productionDirectory);
        Client client = viewTask.getClient();
        client.setManager(manager);
        client = entityManager.persistAndFlush(client);
        viewTask.setClient(client);
        State state = entityManager.persistAndFlush(viewTask.getState());
        viewTask.setState(state);
        Payment payment = entityManager.persistAndFlush(viewTask.getPayment());
        viewTask.setPayment(payment);
        return viewTask;
    }
}
