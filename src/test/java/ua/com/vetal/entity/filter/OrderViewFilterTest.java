package ua.com.vetal.entity.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.entity.StatisticOrder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderViewFilterTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void onHasData() {
        OrderViewFilter orderViewFilter = new OrderViewFilter();
        assertFalse(orderViewFilter.hasData());
        orderViewFilter.setAccount("");
        assertFalse(orderViewFilter.hasData());

        orderViewFilter.setAccount("TestAccount");
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setAccount(null);
        orderViewFilter.setNumber("");
        assertFalse(orderViewFilter.hasData());

        orderViewFilter.setNumber("TestNumber");
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setNumber(null);
        orderViewFilter.setManager(new Manager());
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setManager(null);
        orderViewFilter.setPaper(new PaperDirectory());
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setPaper(null);
        orderViewFilter.setProduction(TestDataUtils.getProductionDirectory(1l));
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setProduction(null);
        orderViewFilter.setClient(TestDataUtils.getClient(1l));
        assertTrue(orderViewFilter.hasData());


        orderViewFilter.setClient(null);
        orderViewFilter.setPrinter(TestDataUtils.getPrinter(1l));
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setPrinter(null);
        orderViewFilter.setContractor(TestDataUtils.getContractor(1l));
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setContractor(null);
        orderViewFilter.setWorkName("TestWorkName");
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setWorkName(null);
        orderViewFilter.setFileName("TestFileName");
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setFileName(null);
        orderViewFilter.setDateBeginFrom(new Date());
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setDateBeginFrom(null);
        orderViewFilter.setDateBeginTill(new Date());
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setDateBeginTill(null);
        orderViewFilter.setDebtAmountFrom(10d);
        assertTrue(orderViewFilter.hasData());

        orderViewFilter.setDebtAmountFrom(null);
        orderViewFilter.setDebtAmountTill(10d);
        assertTrue(orderViewFilter.hasData());
    }


    @Test
    public void onGetDefault() {
        OrderViewFilter orderViewFilter = new OrderViewFilter();
        OrderViewFilter defaultOrderViewFilter = (OrderViewFilter) orderViewFilter.getDefault();
        assertNotNull(defaultOrderViewFilter);
        assertNotNull(defaultOrderViewFilter.getDateBeginFrom());
        assertNull(defaultOrderViewFilter.getManager());
    }

    @Test
    public void onGetPredicate() {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StatisticOrder> query = builder.createQuery(StatisticOrder.class);
		Root<StatisticOrder> root = query.from(StatisticOrder.class);

		OrderViewFilter orderViewFilter = new OrderViewFilter();
		orderViewFilter.setAccount("TestAccount");
		orderViewFilter.setManager(TestDataUtils.getManager(1l));
        Predicate predicate = orderViewFilter.getPredicate(builder, root);
		assertNotNull(predicate);

	}

}