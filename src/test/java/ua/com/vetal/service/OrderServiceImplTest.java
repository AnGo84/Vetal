package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.OrderRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderServiceImpl clientService;
    @MockBean
    private OrderRepository mockOrderRepository;

    private ProductionDirectory production;
    private Manager manager;
    private Client client;
    private Order order;
    private List<Order> orders;

    @BeforeEach
    public void beforeEach() {
        orders = new ArrayList<>();
        ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(null, "Production type");

        production = TestBuildersUtils.getProductionDirectory(null, "fullName", "shortName", productionType);

        manager = TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail");

        client = TestBuildersUtils.getClient(null, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        client.setManager(manager);

        Manager orderManager = TestBuildersUtils.getManager(null, "managerOrderFirstName", "managerOrderLastName", "managerOrderMiddleName", "managerOrderEmail");

        order = TestBuildersUtils.getOrder(null, 100000, client, new Date(), 500,
                "fullNumber", orderManager, "task", 10, production);
        orders.add(order);
        order = TestBuildersUtils.getOrder(null, 200000, client, new Date(), 1000,
                "fullNumber", manager, "stencil", 50, production);
        orders.add(order);
    }

    @Test
    void whenFindAllObjects() {
        when(mockOrderRepository.findAll(any(Sort.class))).thenReturn(orders);
        List<Order> objects = clientService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 2);
    }

    @Disabled("Disabled until refactoring filters")
    @Test
    void whenFindByFilterData() {
        List<Order> filteredList = clientService.findByFilterData(null);
        assertEquals(filteredList.size(), 2);

        FilterData filterData = new FilterData();
        filterData.setClient(client);
        filterData.setManager(manager);

        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setClient(client);
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(client.getManager());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(new Manager());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setClient(new Client());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());
    }

}