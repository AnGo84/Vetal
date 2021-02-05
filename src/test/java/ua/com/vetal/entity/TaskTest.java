package ua.com.vetal.entity;

import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void whenConstructFullNumber_thenReturnResult() {
        Task task = TestDataUtils.getTask(1l, 12);
        String fullNumber = task.constructFullNumber();
        assertNotNull(fullNumber);
        assertTrue(fullNumber.contains(String.valueOf(task.getNumber())));
        assertTrue(fullNumber.contains(task.getNumberBase().getName()));
        assertTrue(fullNumber.contains(String.valueOf(task.getNumberSuffix())));
    }

    @Test
    public void whenCopy_thenReturnResult() {
        Task task = TestDataUtils.getTask(1l, 123);
        Task newTask = task.getCopy();

        assertNull(newTask.id);
        assertEquals(task.manager, newTask.manager);
        assertEquals(task.production, newTask.production);
        assertEquals(task.dateBegin, newTask.dateBegin);
        assertEquals(task.dateEnd, newTask.dateEnd);
        assertEquals(task.client, newTask.client);
        assertEquals(task.stock, newTask.stock);
        assertEquals(task.printing, newTask.printing);
        assertEquals(task.printingUnit, newTask.printingUnit);
        assertEquals(task.paper, newTask.paper);
        assertEquals(task.fillet, newTask.fillet);
        assertEquals(task.popup, newTask.popup);
        assertEquals(task.carving, newTask.carving);
        assertEquals(task.stamping, newTask.stamping);
        assertEquals(task.embossing, newTask.embossing);
        assertEquals(task.bending, newTask.bending);
        assertEquals(task.plotter, newTask.plotter);
        assertEquals(task.packBox, newTask.packBox);
        assertEquals(task.packPellicle, newTask.packPellicle);
        assertEquals(task.packPackage, newTask.packPackage);
        assertEquals(task.packNP, newTask.packNP);
        assertEquals(task.numeration, newTask.numeration);
        assertEquals(task.numerationStart, newTask.numerationStart);
        assertEquals(task.amount, newTask.amount);
        //
        assertEquals(0, newTask.number);
        assertNotEquals(task.number, newTask.number);

        assertNull(newTask.numberBase);
        assertNotEquals(task.numberBase, newTask.numberBase);

        assertEquals(0, newTask.numberSuffix);
        assertNotEquals(task.numberSuffix, newTask.numberSuffix);

        assertTrue(StringUtils.isEmpty(newTask.fullNumber));
        assertNotEquals(task.fullNumber, newTask.fullNumber);

        assertTrue(StringUtils.isEmpty(newTask.account));
        assertNotEquals(task.account, newTask.account);

        assertNull(newTask.state);
        assertNotEquals(task.state, newTask.state);

        assertNull(newTask.payment);
        assertNotEquals(task.payment, newTask.payment);

        assertNull(newTask.debtAmount);
        assertNotEquals(task.debtAmount, newTask.debtAmount);

        assertNull(newTask.otherExpenses);
        assertNotEquals(task.otherExpenses, newTask.otherExpenses);

        // specific for task
        assertEquals(task.getWorkName(), newTask.getWorkName());
        assertEquals(task.getFileName(), newTask.getFileName());
        assertEquals(task.getContractor(), newTask.getContractor());
        assertEquals(task.getProductionType(), newTask.getProductionType());
        assertEquals(task.getChromaticity(), newTask.getChromaticity());
        assertEquals(task.getFormat(), newTask.getFormat());
        assertEquals(task.getPrintingFormat(), newTask.getPrintingFormat());
        assertEquals(task.getLaminate(), newTask.getLaminate());
        assertEquals(task.getWares(), newTask.getWares());
        assertEquals(task.getCringle(), newTask.getCringle());
        assertEquals(task.isAssembly(), newTask.isAssembly());
        assertEquals(task.isCutting(), newTask.isCutting());
        assertEquals(task.getNote(), newTask.getNote());
        assertEquals(task.getPackBy(), newTask.getPackBy());

        //
        assertNull(newTask.getDbFile());
        assertNotEquals(task.getDbFile(), newTask.getDbFile());

        assertNull(newTask.getContractorNumber());
        assertNotEquals(task.getContractorNumber(), newTask.getContractorNumber());

        assertNull(newTask.getContractorNumber());
        assertNotEquals(task.getContractorNumber(), newTask.getContractorNumber());

        assertNull(newTask.getAmountForContractor());
        assertNotEquals(task.getAmountForContractor(), newTask.getAmountForContractor());

        assertNull(newTask.getContractorNumber());
        assertNotEquals(task.getContractorNumber(), newTask.getContractorNumber());

        assertNull(newTask.getProvider());
        assertNotEquals(task.getProvider(), newTask.getProvider());

        assertNull(newTask.getProviderCost());
        assertNotEquals(task.getProviderCost(), newTask.getProviderCost());

    }
}