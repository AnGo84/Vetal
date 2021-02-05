package ua.com.vetal.entity;

import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class StencilTest {

    @Test
    public void whenConstructFullNumber_thenReturnResult() {
        Stencil stencil = TestDataUtils.getStencil(1l, 12);
        String fullNumber = stencil.constructFullNumber();
        assertNotNull(fullNumber);
        assertTrue(fullNumber.contains(String.valueOf(stencil.getNumber())));
        assertTrue(fullNumber.contains(stencil.getNumberBase().getName()));
        assertTrue(fullNumber.contains(String.valueOf(stencil.getNumberSuffix())));
    }

    @Test
    public void whenCopy_thenReturnResult() {
        Stencil stencil = TestDataUtils.getStencil(1l, 123);
        Stencil newStencil = stencil.getCopy();

        assertNull(newStencil.id);
        assertEquals(stencil.manager, newStencil.manager);
        assertEquals(stencil.production, newStencil.production);
        assertEquals(stencil.dateBegin, newStencil.dateBegin);
        assertEquals(stencil.dateEnd, newStencil.dateEnd);
        assertEquals(stencil.client, newStencil.client);
        assertEquals(stencil.stock, newStencil.stock);
        assertEquals(stencil.printing, newStencil.printing);
        assertEquals(stencil.printingUnit, newStencil.printingUnit);
        assertEquals(stencil.paper, newStencil.paper);
        assertEquals(stencil.fillet, newStencil.fillet);
        assertEquals(stencil.popup, newStencil.popup);
        assertEquals(stencil.carving, newStencil.carving);
        assertEquals(stencil.stamping, newStencil.stamping);
        assertEquals(stencil.embossing, newStencil.embossing);
        assertEquals(stencil.bending, newStencil.bending);
        assertEquals(stencil.plotter, newStencil.plotter);
        assertEquals(stencil.packBox, newStencil.packBox);
        assertEquals(stencil.packPellicle, newStencil.packPellicle);
        assertEquals(stencil.packPackage, newStencil.packPackage);
        assertEquals(stencil.packNP, newStencil.packNP);
        assertEquals(stencil.numeration, newStencil.numeration);
        assertEquals(stencil.numerationStart, newStencil.numerationStart);
        assertEquals(stencil.amount, newStencil.amount);
        //
        assertEquals(0, newStencil.number);
        assertNotEquals(stencil.number, newStencil.number);

        assertEquals(0, newStencil.numberSuffix);
        assertNotEquals(stencil.numberSuffix, newStencil.numberSuffix);

        assertTrue(StringUtils.isEmpty(newStencil.fullNumber));
        assertNotEquals(stencil.fullNumber, newStencil.fullNumber);

        assertTrue(StringUtils.isEmpty(newStencil.account));
        assertNotEquals(stencil.account, newStencil.account);

        assertNull(newStencil.state);
        assertNotEquals(stencil.state, newStencil.state);

        assertNull(newStencil.payment);
        assertNotEquals(stencil.payment, newStencil.payment);

        assertNull(newStencil.debtAmount);
        assertNotEquals(stencil.debtAmount, newStencil.debtAmount);

        assertNull(newStencil.otherExpenses);
        assertNotEquals(stencil.otherExpenses, newStencil.otherExpenses);

        // specific for stencil
        assertEquals(stencil.getNumberBase(), newStencil.getNumberBase());
        assertEquals(stencil.getOrderName(), newStencil.getOrderName());
        assertEquals(stencil.getPrintingNote(), newStencil.getPrintingNote());
        assertEquals(stencil.getPaperFormat(), newStencil.getPaperFormat());
        assertEquals(stencil.getSheetNumber(), newStencil.getSheetNumber());
        assertEquals(stencil.getPrinter(), newStencil.getPrinter());
        assertEquals(stencil.getAdjustment(), newStencil.getAdjustment());
        assertEquals(stencil.getDatePrintBegin(), newStencil.getDatePrintBegin());
        assertEquals(stencil.getWorkerPrint(), newStencil.getWorkerPrint());
        assertEquals(stencil.getWorkerCut(), newStencil.getWorkerCut());
        assertEquals(stencil.isCutRibbon(), newStencil.isCutRibbon());
        assertEquals(stencil.getRibbonLength(), newStencil.getRibbonLength());
        assertEquals(stencil.isSticker(), newStencil.isSticker());
        assertEquals(stencil.getKraskoottisk(), newStencil.getKraskoottisk());

        //
        assertNull(newStencil.getCostOfMaterials());
        assertNotEquals(stencil.getCostOfMaterials(), newStencil.getCostOfMaterials());

        assertNull(newStencil.getCostOfPrinting());
        assertNotEquals(stencil.getCostOfPrinting(), newStencil.getCostOfPrinting());

        assertNull(newStencil.getProductionAvailability());
        assertNotEquals(stencil.getProductionAvailability(), newStencil.getProductionAvailability());

    }
}