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
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.StencilRepository;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class StencilDAOTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private StencilRepository stencilRepository;
    @Autowired
    private StencilDAO stencilDAO;

    private Stencil stencil;

    @BeforeEach
    public void beforeEach() {
        stencilRepository.deleteAll();
        stencil = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), testEntityManager));
        stencil = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), testEntityManager));
    }

    @Test
    public void whenGetMaxID_thenReturnNewID() {
        Long newID = stencilDAO.getMaxID();
        assertNotNull(newID);
        assertEquals(stencil.getId(), newID);
    }

    @Test
    void whenFindByFilterData() {
        int allRecords = 2;
        List<Stencil> filteredList = stencilDAO.findByFilterData(null);
        assertEquals(allRecords, filteredList.size());

        OrderViewFilter orderViewFilter = new OrderViewFilter();
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setAccount(stencil.getAccount());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setNumber(stencil.getFullNumber());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());


        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setClient(stencil.getClient());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setPrinter(stencil.getPrinter());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(stencil.getManager());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setManager(new Manager());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        orderViewFilter.setManager(newManager);
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setAccount("not exist");
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setNumber("not exist");
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(0, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setPaper(stencil.getPaper());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setProduction(stencil.getProduction());
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());


        Date dateFrom = DateUtils.addToDate(new Date(), Calendar.DATE, -11);
        Date dateTill = DateUtils.addToDate(new Date(), Calendar.DATE, -5);


        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(allRecords, filteredList.size());

        orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(dateFrom);
        orderViewFilter.setDateBeginTill(dateTill);
        filteredList = stencilDAO.findByFilterData(orderViewFilter);
        assertEquals(1, filteredList.size());

    }


}