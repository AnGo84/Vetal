package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.StatisticOrder;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.OrderRepository;
import ua.com.vetal.repositories.StencilRepository;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class StatisticStatisticDAOTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private StatisticDAO statisticDAO;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StencilRepository stencilRepository;

    private Task task;
	private Stencil stencil;
	private StatisticOrder p;

    @BeforeEach
    public void beforeEach() {
        taskRepository.deleteAll();
        stencilRepository.deleteAll();

        task = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), testEntityManager);
        task = testEntityManager.persistAndFlush(task);
        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromTask(task));

        Task tempTask = testEntityManager.persistAndFlush(TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), testEntityManager));
        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromTask(tempTask));

        stencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), testEntityManager);
        stencil = testEntityManager.persistAndFlush(stencil);
        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromStencil(stencil));

        Stencil tempStencil = testEntityManager.persistAndFlush(TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), testEntityManager));
        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromStencil(tempStencil));
    }

    @Test
    void whenFindByFilterData() {
        int allRecords = 4;
        List<StatisticOrder> filteredList = statisticDAO.findByFilterData(null);
        assertTrue(filteredList.isEmpty());
        assertEquals(0, filteredList.size());

        OrderViewFilter orderViewFilter = new OrderViewFilter();
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setClient(task.getClient());
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(task.getManager());
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(new Manager());
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        orderViewFilter.setManager(newManager);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setProduction(task.getProduction());
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        Date dateFrom = DateUtils.addToDate(new Date(), Calendar.DATE, -11);
        Date dateTill = DateUtils.addToDate(new Date(), Calendar.DATE, -5);

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(2, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(2, filteredList.size());

        double debtFrom = 4 * 2;
        double debtTill = 6 * 2;
        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDebtAmountFrom(debtFrom);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(2, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDebtAmountTill(debtTill);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDebtAmountFrom(debtFrom);
        orderViewFilter.setDebtAmountTill(debtTill);
        filteredList = statisticDAO.findByFilterData(orderViewFilter);
        assertEquals(2, filteredList.size());

    }
}