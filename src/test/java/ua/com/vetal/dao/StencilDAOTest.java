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
import ua.com.vetal.entity.filter.FilterData;
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

        FilterData filterData = new FilterData();
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount(stencil.getAccount());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setNumber(stencil.getFullNumber());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());


        filterData = new FilterData();
        filterData.setClient(stencil.getClient());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setPrinter(stencil.getPrinter());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(stencil.getManager());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(new Manager());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        filterData.setManager(newManager);
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount("not exist");
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setNumber("not exist");
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setPaper(stencil.getPaper());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setProduction(stencil.getProduction());
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());


        Date dateFrom = DateUtils.addToDate(new Date(), Calendar.DATE, -11);
        Date dateTill = DateUtils.addToDate(new Date(), Calendar.DATE, -5);


        filterData = new FilterData();
        filterData.setDateBeginFrom(dateFrom);
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setDateBeginTill(dateTill);
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new FilterData();
        filterData.setDateBeginFrom(dateFrom);
        filterData.setDateBeginTill(dateTill);
        filteredList = stencilDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

    }


}