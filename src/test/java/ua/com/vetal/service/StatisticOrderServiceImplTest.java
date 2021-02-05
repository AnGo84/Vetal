package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.dao.StatisticDAO;
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.OrderRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StatisticOrderServiceImplTest {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private OrderServiceImpl orderService;
    @MockBean
    private OrderRepository mockOrderRepository;
    @MockBean
    private StatisticDAO mockStatisticDAO;

	private ProductionDirectory production;
	private Manager manager;
	private Client client;
	private StatisticOrder statisticOrder;
	private List<StatisticOrder> statisticOrders;

	@BeforeEach
	public void beforeEach() {
		statisticOrders = new ArrayList<>();
		ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(null, "Production type");

		production = TestBuildersUtils.getProductionDirectory(null, "fullName", "shortName", productionType);

		manager = TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail");

		client = TestBuildersUtils.getClient(null, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
		client.setManager(manager);

		Manager orderManager = TestBuildersUtils.getManager(null, "managerOrderFirstName", "managerOrderLastName", "managerOrderMiddleName", "managerOrderEmail");

		statisticOrder = TestBuildersUtils.getOrder(null, 100000, client, new Date(), 500,
				"fullNumber", orderManager, "task", 10, production);
		statisticOrders.add(statisticOrder);
		statisticOrder = TestBuildersUtils.getOrder(null, 200000, client, new Date(), 1000,
				"fullNumber", manager, "stencil", 50, production);
		statisticOrders.add(statisticOrder);
	}

    @Test
    void whenFindAllObjects() {
		when(mockOrderRepository.findAll(any(Sort.class))).thenReturn(statisticOrders);
		List<StatisticOrder> objects = orderService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 2);
	}

    @Test
    void whenFindByFilterData() {
        when(mockStatisticDAO.findByFilterData(any(OrderViewFilter.class))).thenReturn(Arrays.asList(statisticOrder));
        List<StatisticOrder> objects = orderService.findByFilterData(new OrderViewFilter());
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

}