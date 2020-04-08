package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OrderRepository orderRepository;

    private ProductionDirectory production;
    private Manager manager;
    private Client client;
    private Order order;

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();
        ProductionTypeDirectory productionType = testEntityManager.persistAndFlush(TestBuildersUtils.getProductionTypeDirectory(null, "Production type"));

        production = TestBuildersUtils.getProductionDirectory(null, "fullName", "shortName", productionType);
        production = testEntityManager.persistAndFlush(production);

        manager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail"));

        client = TestBuildersUtils.getClient(null, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        client.setManager(manager);
        client = testEntityManager.persistAndFlush(client);

        Manager orderManager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerOrderFirstName", "managerOrderLastName", "managerOrderMiddleName", "managerOrderEmail"));

        order = TestBuildersUtils.getOrder(null, 100000, client, new Date(), 500,
                "fullNumber", orderManager, "task", 10, production);
        order = testEntityManager.persistAndFlush(order);

    }

    @Disabled("Need to use task and stencils for get orders")
    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        Order newOrder = TestBuildersUtils.getOrder(null, 200000, client, new Date(), 1000,
                "fullNumber", manager, "stencil", 50, production);
        order = testEntityManager.persistAndFlush(order);
        List<Order> orders = orderRepository.findAll();
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), 2);

    }

    @Disabled("Need to use task and stencils for get orders")
    @Test
    public void whenFindAllWithSortOption_thenReturnListOfManagers() {
        Order newOrder = TestBuildersUtils.getOrder(null, 200000, client, new Date(), 1000,
                "fullNumber", manager, "stencil", 50, production);
        order = testEntityManager.persistAndFlush(order);
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), 2);

    }
}