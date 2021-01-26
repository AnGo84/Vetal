package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class TaskDAOTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskDAO taskDAO;

    private Task task;

    @BeforeEach
    public void beforeEach() {
        taskRepository.deleteAll();
        task = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), testEntityManager));
        task = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), testEntityManager));
    }

    @Test
    public void whenGetMaxID_thenReturnNewID() {
        Long newID = taskDAO.getMaxID();
        assertNotNull(newID);
        assertEquals(task.getId(), newID);
    }


    @Test
    void whenFindByFilterData() {
        int allRecords = 2;
        List<Task> filteredList = taskDAO.findByFilterData(null);
        assertTrue(filteredList.isEmpty());
        assertEquals(0, filteredList.size());

        OrderViewFilter orderViewFilter = new OrderViewFilter();
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setAccount(task.getAccount());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setNumber(task.getFullNumber());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());


        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setClient(task.getClient());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setFileName(task.getFileName());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(task.getManager());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(new Manager());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        orderViewFilter.setManager(newManager);
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setAccount("not exist");
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setNumber("not exist");
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setPaper(task.getPaper());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setProduction(task.getProduction());
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());


        Date dateFrom = DateUtils.addToDate(new Date(), Calendar.DATE, -11);
        Date dateTill = DateUtils.addToDate(new Date(), Calendar.DATE, -5);


        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = taskDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());
    }
}