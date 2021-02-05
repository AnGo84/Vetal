package ua.com.vetal.entity;

import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.utils.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

	@Test
	public void whenConstructFullNumber_thenReturnResult() {
		Order order = TestDataUtils.getOrder(1l, 12);
		String fullNumber = order.constructFullNumber();
		assertNotNull(fullNumber);
		assertTrue(fullNumber.contains(String.valueOf(order.getNumber())));
		assertTrue(fullNumber.contains(order.getNumberBase().getName()));
		assertTrue(fullNumber.contains(String.valueOf(order.getNumberSuffix())));
	}

	@Test
	public void whenCopyCommonFields_thenReturnResult() {
		Order order = TestDataUtils.getOrder(1l, 123);
		Order newOrder = new Order();
		order.copyCommonFields(order, newOrder);

		assertNull(newOrder.id);
		assertEquals(order.manager, newOrder.manager);
		assertEquals(order.production, newOrder.production);
		assertEquals(order.dateBegin, newOrder.dateBegin);
		assertEquals(order.dateEnd, newOrder.dateEnd);
		assertEquals(order.client, newOrder.client);
		assertEquals(order.stock, newOrder.stock);
		assertEquals(order.printing, newOrder.printing);
		assertEquals(order.printingUnit, newOrder.printingUnit);
		assertEquals(order.paper, newOrder.paper);
		assertEquals(order.fillet, newOrder.fillet);
		assertEquals(order.popup, newOrder.popup);
		assertEquals(order.carving, newOrder.carving);
		assertEquals(order.stamping, newOrder.stamping);
		assertEquals(order.embossing, newOrder.embossing);
		assertEquals(order.bending, newOrder.bending);
		assertEquals(order.plotter, newOrder.plotter);
		assertEquals(order.packBox, newOrder.packBox);
		assertEquals(order.packPellicle, newOrder.packPellicle);
		assertEquals(order.packPackage, newOrder.packPackage);
		assertEquals(order.packNP, newOrder.packNP);
		assertEquals(order.numeration, newOrder.numeration);
		assertEquals(order.numerationStart, newOrder.numerationStart);
		assertEquals(order.amount, newOrder.amount);
		//
		assertEquals(0, newOrder.number);
		assertNotEquals(order.number, newOrder.number);

		assertNull(newOrder.numberBase);
		assertNotEquals(order.numberBase, newOrder.numberBase);

		assertEquals(0, newOrder.numberSuffix);
		assertNotEquals(order.numberSuffix, newOrder.numberSuffix);

		assertTrue(StringUtils.isEmpty(newOrder.fullNumber));
		assertNotEquals(order.fullNumber, newOrder.fullNumber);

		assertTrue(StringUtils.isEmpty(newOrder.account));
		assertNotEquals(order.account, newOrder.account);

		assertNull(newOrder.state);
		assertNotEquals(order.state, newOrder.state);

		assertNull(newOrder.payment);
		assertNotEquals(order.payment, newOrder.payment);

		assertNull(newOrder.debtAmount);
		assertNotEquals(order.debtAmount, newOrder.debtAmount);


		assertNull(newOrder.otherExpenses);
		assertNotEquals(order.otherExpenses, newOrder.otherExpenses);

	}

}