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
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals(allRecords, filteredList.size());

        FilterData filterData = new FilterData();
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount(task.getAccount());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setNumber(task.getFullNumber());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());


        filterData = new FilterData();
        filterData.setClient(task.getClient());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setFileName(task.getFileName());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(task.getManager());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(new Manager());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        filterData.setManager(newManager);
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount("not exist");
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setNumber("not exist");
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setPaper(task.getPaper());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setProduction(task.getProduction());
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());


        Date dateFrom = DateUtils.addToDate(new Date(), Calendar.DATE, -11);
        Date dateTill = DateUtils.addToDate(new Date(), Calendar.DATE, -5);


        filterData = new FilterData();
        filterData.setDateBeginFrom(dateFrom);
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setDateBeginTill(dateTill);
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        filterData.setDateBeginFrom(dateFrom);
        filterData.setDateBeginTill(dateTill);
        filteredList = taskDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

    }
}