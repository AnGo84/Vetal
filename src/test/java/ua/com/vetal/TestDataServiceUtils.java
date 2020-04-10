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
}
